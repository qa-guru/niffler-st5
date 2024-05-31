package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepositoryHibernate;

public class DbSpendExtension extends AbstractSpendExtension {

	private final SpendRepositoryHibernate srh = new SpendRepositoryHibernate();

	@Override
	protected Object createSpend(SpendEntity spendEntity) {
		return srh.createSpend(spendEntity);
	}

	@Override
	protected void removeSpend(SpendEntity spend) {
		srh.removeSpend(spend);
	}
}
