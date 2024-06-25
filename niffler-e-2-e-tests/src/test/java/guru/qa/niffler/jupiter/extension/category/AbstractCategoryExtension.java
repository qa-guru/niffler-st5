package guru.qa.niffler.jupiter.extension.category;

import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

public abstract class AbstractCategoryExtension implements BeforeEachCallback, AfterEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(AbstractCategoryExtension.class);

    protected abstract CategoryJson createCategory(CategoryJson categoryJson);

    protected abstract void removeCategory(CategoryJson categoryJson);

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        findAnnotation(extensionContext.getRequiredTestMethod(), GenerateCategory.class
        ).ifPresent(generateCategory -> {
                    CategoryJson category = new CategoryJson(
                            null,
                            generateCategory.category(),
                            generateCategory.username()
                    );

                    category = createCategory(category);

                    extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), category);
                }
        );
    }

    @Override
    public void afterEach(ExtensionContext context) {
        CategoryJson categoryJson = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        removeCategory(categoryJson);
    }

}