package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;

public class CategoryJdbcExtension extends AbstractCategoryExtension {

    private final SpendRepository spendRepository = SpendRepository.getInstance();

    @Override
    protected CategoryJson createCategory(Category category) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategory(category.category());
        categoryEntity.setUsername(category.username());

        // присваиваем ответ из бд - categoryEntity теперь уже с id
        categoryEntity = spendRepository.createCategory(categoryEntity);

        return CategoryJson.fromEntity(categoryEntity);
    }

    @Override
    protected void removeCategory(CategoryJson category) {
        if(category == null) return;

        CategoryJson categoryJson =
                new CategoryJson(category.id(), category.category(), category.username());

        spendRepository.removeCategory(CategoryEntity.fromJson(categoryJson));
    }
}
