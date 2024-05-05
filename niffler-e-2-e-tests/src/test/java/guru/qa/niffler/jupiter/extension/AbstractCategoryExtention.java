package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

public abstract class AbstractCategoryExtention implements BeforeEachCallback, AfterEachCallback{
    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(AbstractCategoryExtention.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                Category.class
        ).ifPresent(
                cat -> {
                    extensionContext.getStore(NAMESPACE).put(
                            extensionContext.getUniqueId(), createCategory(CategoryJson.fromEntity(CategoryEntity.fromCategory(cat)))
                    );
                }
        );
    }

    @Override
    public void afterEach(ExtensionContext context) {
        CategoryJson categoryJson = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        removeCategory(categoryJson);
    }

    protected abstract CategoryJson createCategory(CategoryJson category);

    protected abstract void removeCategory(CategoryJson category);
}
