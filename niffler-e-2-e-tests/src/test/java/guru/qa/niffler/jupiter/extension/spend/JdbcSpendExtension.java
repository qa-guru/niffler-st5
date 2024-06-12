package guru.qa.niffler.jupiter.extension.spend;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.spend.SpendRepository;
import guru.qa.niffler.model.SpendJson;

import static guru.qa.niffler.data.repository.RepositoryType.SPRING_JDBC;

public class JdbcSpendExtension extends AbstractSpendExtension {

    private final SpendRepository spendRepository = SpendRepository.getInstance(SPRING_JDBC);

    @Override
    protected SpendJson createSpend(SpendJson spendJson) {
        SpendEntity spendEntity = SpendEntity.fromJson(spendJson);
        spendEntity = spendRepository.createSpend(spendEntity);
        return SpendJson.fromEntity(spendEntity);
    }

    @Override
    protected void removeSpend(SpendJson spendJson) {
        spendRepository.removeSpend(SpendEntity.fromJson(spendJson));
    }

}