package guru.qa.niffler.extensions;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.retrofit.categoriesEndpoint.CategoriesClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoriesExtension implements BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(CategoriesExtension.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {

        AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                Category.class
        ).ifPresent(
                category -> {
                    var cat = new CategoriesClient()
                            .addNewCategory(category.category(), category.username());
                    extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), cat);
                });
    }

}


