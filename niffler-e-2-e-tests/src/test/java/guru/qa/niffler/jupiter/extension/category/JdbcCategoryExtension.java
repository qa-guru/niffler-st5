package guru.qa.niffler.jupiter.extension.category;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.repository.spend.SpendRepository;
import guru.qa.niffler.model.CategoryJson;

import static guru.qa.niffler.data.repository.RepositoryType.HIBERNATE;

public class JdbcCategoryExtension extends AbstractCategoryExtension {

    @Override
    protected CategoryJson createCategory(CategoryJson categoryJson) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
        categoryEntity = SpendRepository.getInstance(HIBERNATE).createCategory(categoryEntity);
        return CategoryJson.fromEntity(categoryEntity);
    }

    @Override
    protected void removeCategory(CategoryJson categoryJson) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
        SpendRepository.getInstance(HIBERNATE).removeCategory(categoryEntity);
    }

}