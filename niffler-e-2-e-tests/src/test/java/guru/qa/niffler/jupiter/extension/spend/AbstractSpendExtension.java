package guru.qa.niffler.jupiter.extension.spend;

import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.extension.category.JdbcCategoryExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public abstract class AbstractSpendExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(HttpSpendExtension.class);

    protected abstract SpendJson createSpend(SpendJson spendJson);

    protected abstract void removeSpend(SpendJson spendJson);

    @Override
    public void beforeEach(ExtensionContext context) {
        CategoryJson categoryJson = context.getStore(JdbcCategoryExtension.NAMESPACE).get(
                context.getUniqueId(),
                CategoryJson.class);

        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                GenerateSpend.class
        ).ifPresent(
                generateSpend -> {
                    SpendJson spendJson = new SpendJson(
                            null,
                            new Date(),
                            categoryJson.categoryName(),
                            generateSpend.currency(),
                            generateSpend.amount(),
                            generateSpend.description(),
                            categoryJson.username(),
                            categoryJson.id());

                    SpendJson spend = createSpend(spendJson);

                    context.getStore(NAMESPACE).put(context.getUniqueId(), spend);
                }
        );
    }

    @Override
    public void afterEach(ExtensionContext context) {
        SpendJson spendJson = context.getStore(NAMESPACE).get(context.getUniqueId(), SpendJson.class);
        removeSpend(spendJson);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId());
    }

}