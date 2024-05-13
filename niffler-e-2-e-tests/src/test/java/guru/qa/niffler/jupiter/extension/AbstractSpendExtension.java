package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public abstract class AbstractSpendExtension implements BeforeEachCallback, ParameterResolver, AfterEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(AbstractSpendExtension.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), Spend.class)
                .ifPresent(spend -> extensionContext
                        .getStore(NAMESPACE)
                        .put(extensionContext.getUniqueId(), createSpend(SpendJson.fromEntity(SpendEntity.fromSpend(spend))))
                );
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        SpendJson spendJson = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), SpendJson.class);
        removeSpend(spendJson);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(SpendJson.class) || parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if (parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(CategoryJson.class)) {
            return CategoryJson.fromEntity(((SpendJson) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId())).categoryEntity());
        } else {
            return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId());
        }
    }

    protected abstract SpendJson createSpend(SpendJson spend);

    protected abstract void removeSpend(SpendJson spend);
}
