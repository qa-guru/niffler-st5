package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spend;
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

public abstract class SpendExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(SpendExtension.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        CategoryJson category = extensionContext.getStore(CategoryExtension.NAMESPACE).get(
                extensionContext.getUniqueId(),
                CategoryJson.class
        );

        AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                Spend.class
        ).ifPresent(
                spend -> {
                    SpendJson spendJson = new SpendJson(
                            null,
                            new Date(),
                            category.category(),
                            spend.currency(),
                            spend.amount(),
                            spend.description(),
                            category.username()
                    );
                    extensionContext
                            .getStore(NAMESPACE)
                            .put(extensionContext.getUniqueId(), createSpend(spendJson));
                }
        );
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        SpendJson spendJson = context.getStore(NAMESPACE).get(context.getUniqueId(), SpendJson.class);
        removeSpend(spendJson);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(SpendJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId());
    }

    protected abstract SpendJson createSpend(SpendJson spend);

    protected abstract void removeSpend(SpendJson spend);
}
