package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.model.SpendJson;

public class SpendExtensionJdbc extends AbstractSpendExtension {

    private final SpendRepository spendRepository = SpendRepository.getInstance();

    @Override
    protected SpendJson createSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        return SpendJson.fromEntity(spendRepository.createSpend(spendEntity));
    }

    @Override
    protected void removeSpend(SpendJson json) {
        spendRepository.removeSpend(SpendEntity.fromJson(json));
    }

}
