package guru.qa.niffler.jupiter.extension.spend;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.spend.SpendRepository;
import guru.qa.niffler.model.SpendJson;

import java.util.List;

import static guru.qa.niffler.data.repository.RepositoryType.HIBERNATE;

public class JdbcSpendExtension extends AbstractSpendExtension {

    private final SpendRepository spendRepository = SpendRepository.getInstance(HIBERNATE);

    @Override
    protected SpendJson createSpend(SpendJson spendJson) {
        SpendEntity spendEntity = SpendEntity.fromJson(spendJson);
        spendEntity = spendRepository.createSpend(spendEntity);
        return SpendJson.fromEntity(spendEntity);
    }

    @Override
    protected void removeSpend(SpendJson spendJson) {
        List<SpendEntity> spendEntityList = spendRepository.findAllByUsername(spendJson.username());
        if (!spendEntityList.isEmpty()) {
            spendRepository.removeSpend(SpendEntity.fromJson(spendJson));
        }
    }

}