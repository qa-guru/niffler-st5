package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.Spends;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractSpendExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    // Создаем пространство имен для хранения данных расходов
    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(AbstractSpendExtension.class);

    // Метод, вызываемый перед каждым тестом
    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        // Получаем категорию из хранилища расширения AbstractCategoryExtension
        CategoryJson category = extensionContext.getStore(AbstractCategoryExtension.NAMESPACE)
                .get(extensionContext.getUniqueId(), CategoryJson.class);

        // Находим аннотацию Spend на методе теста и создаем объект расхода
//        AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), Spend.class)
//                .ifPresent(spend -> extensionContext
//                        .getStore(NAMESPACE)
//                        .put(extensionContext.getUniqueId(), createSpend(spend, category))
//                );

        List<Spend> potentialSpend = new ArrayList<>();

        // Находим аннотацию Spends or Spend на методе теста и создаем объект(ы) расхода
        AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), Spends.class)
                .ifPresent(spends -> potentialSpend.addAll(Arrays.stream(spends.value()).toList()));

        AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), Spend.class)
                .ifPresent(potentialSpend::add);

        if (!potentialSpend.isEmpty()) {
            List<SpendJson> created = new ArrayList<>();

            for (Spend spend : potentialSpend) {
                created.add(createSpend(spend, category));
            }

            extensionContext
                    .getStore(NAMESPACE)
                    .put(extensionContext.getUniqueId(), created);
        }

    }

    // Метод, вызываемый после выполнения каждого теста
    @Override
    @SuppressWarnings("unchecked")
    public void afterEach(ExtensionContext context) {
        // Получаем список расходов из хранилища
        List<SpendJson> spendJsons = context.getStore(NAMESPACE).get(context.getUniqueId(), List.class);
        // Удаляем расход(ы)
        if (!spendJsons.isEmpty()) spendJsons.forEach(this::removeSpend);
    }

    // Абстрактный метод для создания объекта расхода
    protected abstract SpendJson createSpend(Spend spend, CategoryJson category);

    protected abstract SpendJson createSpend(SpendJson spend);

    // Абстрактный метод для удаления объекта расхода
    protected abstract void removeSpend(SpendJson spend);

    // Метод, проверяющий, поддерживается ли параметр типа SpendJson
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> type = parameterContext
                .getParameter()
                .getType();

        return type.isAssignableFrom(SpendJson.class) || type.isAssignableFrom(SpendJson[].class);
    }

    // Метод, возвращающий объект(ы) расхода для параметра типа SpendJson / SpendJson[]
    @Override
    @SuppressWarnings("unchecked")
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> type = parameterContext
                .getParameter()
                .getType();

        List<SpendJson> createdSpends = extensionContext
                .getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), List.class);

        return type.isAssignableFrom(SpendJson.class)
                ? createdSpends.getFirst()
                : createdSpends.toArray(SpendJson[]::new);
    }

}