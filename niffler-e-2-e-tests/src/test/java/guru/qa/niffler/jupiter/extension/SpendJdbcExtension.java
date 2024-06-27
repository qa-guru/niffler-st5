package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.ExtensionContext;

import static guru.qa.niffler.utils.DateHelper.convertStringToDate;

public class SpendJdbcExtension extends AbstractSpendExtension {

    private final ThreadLocal<SpendRepository> spendRepositoryThreadLocal = new ThreadLocal<>();

    @Override
    protected SpendJson createSpend(ExtensionContext context, Spend spend, CategoryJson category) {
        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setSpendDate(convertStringToDate(spend.spendDate()));
        spendEntity.setCategory(CategoryEntity.fromJson(category));
        spendEntity.setCurrency(spend.currency());
        spendEntity.setAmount(spend.amount());
        spendEntity.setDescription(spend.description());
        spendEntity.setUsername(spend.username());

        // присваиваем ответ из бд - spendEntity теперь уже с id
        spendEntity = getThreadLocalRepoInstance().createSpend(spendEntity);

        return SpendJson.fromEntity(spendEntity);
    }

    @Override
    protected SpendJson createSpend(SpendJson spend) {
        return null;
    }

    @Override
    protected void removeSpend(SpendJson spend) {
        if (spend == null) return;
        getThreadLocalRepoInstance().removeSpend(spend);
    }

    private SpendRepository getThreadLocalRepoInstance() {
        SpendRepository spendRepository = spendRepositoryThreadLocal.get();
        if (spendRepository == null) {
            spendRepository = SpendRepository.getInstance();
            spendRepositoryThreadLocal.set(spendRepository);
        }
        return spendRepository;
    }
}
