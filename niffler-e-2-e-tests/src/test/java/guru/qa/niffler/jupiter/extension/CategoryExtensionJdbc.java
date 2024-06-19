package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.model.CategoryJson;

public class CategoryExtensionJdbc extends CategoryExtensionAbstract {


    private final SpendRepository spendRepository = SpendRepository.getInstance();

    @Override
    protected CategoryJson createCategory(CategoryJson category) {
        CategoryEntity cat = new CategoryEntity();
        cat.setCategory(category.category());
        cat.setUsername(category.username());

        spendRepository.createCategory(cat);

        return CategoryJson.fromEntity(cat);
    }

    @Override
    protected void removeCategory(CategoryJson json) {
        spendRepository.removeCategory(CategoryEntity.fromJson(json));
    }
}




