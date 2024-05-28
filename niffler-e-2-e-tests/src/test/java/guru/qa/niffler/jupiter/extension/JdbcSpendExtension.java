package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.SpendRepositoryJdbc;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.CategoryJson;

import java.sql.Date;
import java.util.UUID;

public class JdbcSpendExtension extends AbstractSpendExtension {

	private final SpendRepository spendRepository = new SpendRepositoryJdbc();

	@Override
	protected Object createSpend(GenerateSpend spend, CategoryJson category) {
		SpendEntity spendEntity = new SpendEntity();
		CategoryEntity categoryEntity = new CategoryEntity();
		categoryEntity.setId(categoryEntity.getId());
		categoryEntity.setUsername(category.username());
		categoryEntity.setCategory(category.category());
		spendEntity.setUsername(category.username());
		spendEntity.setSpendDate(new Date(System.currentTimeMillis()));
		spendEntity.setCurrency(spend.currency());
		spendEntity.setAmount(spend.amount());
		spendEntity.setDescription(spend.description());
		spendEntity.setCategory(categoryEntity);
		return spendRepository.createSpend(spendEntity);
	}

	@Override
	protected void removeSpend(SpendEntity spend) {
		spendRepository.removeSpend(spend);
	}
}
