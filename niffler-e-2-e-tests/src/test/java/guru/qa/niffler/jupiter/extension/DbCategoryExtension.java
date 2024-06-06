package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.SpendRepositoryHibernate;
import guru.qa.niffler.model.CategoryJson;

public class DbCategoryExtension extends CategoryExtension {

    private final SpendRepository spendRepository = new SpendRepositoryHibernate();

    @Override
    protected CategoryJson createCategory(CategoryJson category) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategory(category.category());
        categoryEntity.setUsername(category.username());
        return CategoryJson.fromEntity(
                spendRepository.createCategory(categoryEntity)
        );
    }

    @Override
    protected void removeCategory(CategoryJson spend) {
        throw new UnsupportedOperationException();
    }
}
