package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public abstract class AbstractCategoryExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(AbstractCategoryExtension.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), Category.class)
                .ifPresent(cat -> extensionContext
                        .getStore(NAMESPACE)
                        .put(extensionContext.getUniqueId(), createCategory(cat))
                );
    }

    @Override
    public void afterEach(ExtensionContext context) {
        CategoryJson categoryJson = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        removeCategory(categoryJson);
    }

    protected abstract CategoryJson createCategory(Category category);

    protected abstract void removeCategory(CategoryJson category);

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
}
