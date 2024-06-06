package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.CurrencyValues;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.SpendRepositoryHibernate;
import guru.qa.niffler.model.SpendJson;

public class DbSpendExtension extends SpendExtension {

    SpendRepository spendRepository = new SpendRepositoryHibernate();

    @Override
    protected SpendJson createSpend(SpendJson spend) {
        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setUsername(spend.username());
        spendEntity.setSpendDate(spend.spendDate());
        spendEntity.setCurrency(CurrencyValues.valueOf(spend.currency().name()));
        spendEntity.setDescription(spend.description());
        spendEntity.setAmount(spend.amount());

        CategoryEntity categoryEntity = spendRepository.findUserCategoryByName(spend.username(), spend.category())
                .orElseThrow();

        spendEntity.setCategory(categoryEntity);

        return SpendJson.fromEntity(
                spendRepository.createSpend(spendEntity)
        );
    }

    @Override
    protected void removeSpend(SpendJson spend) {
        spendRepository.removeSpend(
                spendRepository.findSpendById(spend.id()).orElseThrow()
        );
    }
}
