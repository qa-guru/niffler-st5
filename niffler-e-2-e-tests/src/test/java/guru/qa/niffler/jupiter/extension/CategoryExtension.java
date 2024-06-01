package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.SpendRepositoryHibernate;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

public abstract class CategoryExtension implements BeforeEachCallback, AfterEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(CategoryExtension.class);

    SpendRepository spendRepository = new SpendRepositoryHibernate();

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                Category.class
        ).ifPresent(cat -> {
                    CategoryJson categoryJson = new CategoryJson(
                            null,
                            cat.category(),
                            cat.username()
                    );
            CategoryEntity tempCategoryEntity = spendRepository.createCategory(CategoryEntity.fromJson(categoryJson));
                    extensionContext
                            .getStore(NAMESPACE)
                            .put(extensionContext.getUniqueId(), new CategoryJson(tempCategoryEntity.getId(),
                                    categoryJson.category(), categoryJson.username()));
                }
        );
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        CategoryJson categoryJson = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        removeCategory(categoryJson);
    }

    protected abstract CategoryJson createCategory(CategoryJson spend);

    protected abstract void removeCategory(CategoryJson spend);
}
