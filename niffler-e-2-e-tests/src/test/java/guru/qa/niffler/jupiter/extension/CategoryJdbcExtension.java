package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.repository.Repository;
import guru.qa.niffler.data.repository.RepositoryJdbc;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryJdbcExtension extends AbstractCategoryExtention {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(CategoryJdbcExtension.class);

    private final Repository repository = new RepositoryJdbc();

    @Override
    protected CategoryJson createCategory(CategoryJson category) {
        CategoryEntity entity = new CategoryEntity();
        entity.setCategory(category.category());
        entity.setUsername(category.username());

        entity = repository.createCategory(entity);
        return CategoryJson.fromEntity(entity);
    }

    @Override
    protected void removeCategory(CategoryJson category) {
        //CategoryJson categoryJson = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
        repository.removeCategory(CategoryEntity.fromJson(category));
    }
}
