package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

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

        // Находим аннотацию Spend на методе теста и создает объект расхода
        AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), Spend.class)
                .ifPresent(spend -> extensionContext
                        .getStore(NAMESPACE)
                        .put(extensionContext.getUniqueId(), createSpend(extensionContext, spend, category))
                );
    }

    // Метод, вызываемый после выполнения каждого теста
    @Override
    public void afterEach(ExtensionContext context) {
        // Получаем объект расхода из хранилища
        SpendJson spendJson = context.getStore(NAMESPACE).get(context.getUniqueId(), SpendJson.class);
        // Удаляем расход
        removeSpend(spendJson);
    }

    // Абстрактный метод для создания объекта расхода
    protected abstract SpendJson createSpend(ExtensionContext extensionContext, Spend spend, CategoryJson category);

    // Абстрактный метод для удаления объекта расхода
    protected abstract void removeSpend(SpendJson spend);

    // Метод, проверяющий, поддерживается ли параметр типа SpendJson
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(SpendJson.class);
    }

    // Метод, возвращающий объект расхода для параметра типа SpendJson
    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId());
    }
}