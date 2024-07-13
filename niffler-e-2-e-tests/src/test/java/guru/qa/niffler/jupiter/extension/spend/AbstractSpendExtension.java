package guru.qa.niffler.jupiter.extension.spend;

import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.extension.category.AbstractCategoryExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public abstract class AbstractSpendExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(AbstractSpendExtension.class);

    protected abstract SpendJson createSpend(SpendJson spendJson);

    protected abstract void removeSpend(SpendJson spendJson);

    @Override
    public void beforeEach(ExtensionContext context) {
        CategoryJson categoryJson = context.getStore(AbstractCategoryExtension.NAMESPACE).get(
                context.getUniqueId(),
                CategoryJson.class);
        List<Spend> potentialSpends = new ArrayList<>();

        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                Spends.class
        ).ifPresentOrElse(
                spends -> potentialSpends.addAll(Arrays.stream(spends.value()).toList()),
                () -> AnnotationSupport.findAnnotation(
                        context.getRequiredTestMethod(),
                        Spend.class
                ).ifPresent(potentialSpends::add)
        );

        if (!potentialSpends.isEmpty()) {
            List<SpendJson> generated = new ArrayList<>();
            for (Spend spend : potentialSpends) {
                SpendJson spendJson = new SpendJson(
                        null,
                        new Date(),
                        categoryJson.categoryName(),
                        spend.currency(),
                        spend.amount(),
                        spend.description(),
                        categoryJson.username(),
                        categoryJson.id());
                generated.add(createSpend(spendJson));
            }

            context.getStore(NAMESPACE)
                    .put(context.getUniqueId(), generated);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterEach(ExtensionContext context) {
        List<SpendJson> spends = context.getStore(NAMESPACE)
                .get(context.getUniqueId(), List.class);

        if (spends != null) {
            spends.forEach(this::removeSpend
            );
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> type = parameterContext.getParameter()
                .getType();
        return type.isAssignableFrom(SpendJson.class) || type.isAssignableFrom(SpendJson[].class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> type = parameterContext
                .getParameter()
                .getType();

        List<SpendJson> createdSpends = extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), List.class);

        return type.isAssignableFrom(SpendJson.class)
                ? createdSpends.getFirst()
                : createdSpends.toArray(SpendJson[]::new);
    }

}