package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.Repository;
import guru.qa.niffler.data.repository.RepositoryJdbc;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.ExtensionContext;

public class SpendJdbcExtension extends AbstractSpendExtension{

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(SpendJdbcExtension.class);

    private final Repository repository = new RepositoryJdbc();

    @Override
    protected SpendJson createSpend(SpendJson spend) {
        SpendEntity tempSpend = SpendEntity.fromJson(spend);
        return SpendJson.fromEntity(repository.createSpend(tempSpend));
    }

    @Override
    protected void removeSpend(SpendJson spend) {
        repository.removeSpend(SpendEntity.fromJson(spend));
    }
}