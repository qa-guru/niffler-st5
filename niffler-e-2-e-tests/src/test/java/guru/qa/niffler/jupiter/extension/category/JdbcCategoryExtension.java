package guru.qa.niffler.jupiter.extension.category;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.SpendRepositoryJdbc;
import guru.qa.niffler.model.CategoryJson;

public class JdbcCategoryExtension extends AbstractCategoryExtension {

    private final SpendRepository spendRepository = new SpendRepositoryJdbc();

    @Override
    protected CategoryJson createCategory(CategoryJson categoryJson) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
        categoryEntity = spendRepository.createCategory(categoryEntity);
        return CategoryJson.fromEntity(categoryEntity);
    }

    @Override
    protected void removeCategory(CategoryJson categoryJson) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
        spendRepository.removeCategory(categoryEntity);
    }

}