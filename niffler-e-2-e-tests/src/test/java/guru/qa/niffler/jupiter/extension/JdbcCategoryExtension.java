package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.SpendRepositoryJdbc;
import guru.qa.niffler.model.CategoryJson;

public class JdbcCategoryExtension extends AbstractCategoryExtension{

	private final SpendRepository spendRepository = new SpendRepositoryJdbc();

	@Override
	protected CategoryJson createCategory(CategoryJson category) {
		CategoryEntity categoryEntity = new CategoryEntity();
		categoryEntity.setCategory(category.category());
		categoryEntity.setUsername(category.username());
		return CategoryJson.fromEntity(spendRepository.createCategory(categoryEntity));
	}

	@Override
	protected void removeCategory(CategoryJson category) {
		spendRepository.removeCategory(CategoryEntity.fromJson(category));
	}
}
