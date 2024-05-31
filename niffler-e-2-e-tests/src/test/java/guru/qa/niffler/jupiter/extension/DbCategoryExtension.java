package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.repository.SpendRepositoryHibernate;
import guru.qa.niffler.model.CategoryJson;

public class DbCategoryExtension extends AbstractCategoryExtension {

	private final SpendRepositoryHibernate srh = new SpendRepositoryHibernate();

	@Override
	protected CategoryJson createCategory(CategoryJson category) {
		return CategoryJson.fromEntity(srh.createCategory(CategoryEntity.fromJson(category)));
	}

	@Override
	protected void removeCategory(CategoryJson category) {
		srh.removeCategory(CategoryEntity.fromJson(category));
	}
}
