package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public abstract class AbstractCategoryExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(AbstractCategoryExtension.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) {

        AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(),
                        GenerateCategory.class)
                .ifPresent(cat -> {
                            try {
                                extensionContext
                                        .getStore(NAMESPACE)
                                        .put(extensionContext.getUniqueId(), createCategory(
                                                new CategoryJson(null, cat.category(), cat.username())));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
    }

    @Override
    public void afterEach(ExtensionContext context) {
        CategoryJson json = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (json != null) {
            removeCategory(json);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId());
    }

    protected abstract CategoryJson createCategory(CategoryJson category) throws Exception;

    protected abstract void removeCategory(CategoryJson json);
}
