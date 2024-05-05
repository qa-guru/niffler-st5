package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.Repository;
import guru.qa.niffler.data.repository.RepositoryJdbc;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class SpendJdbcExtension extends AbstractSpendExtension{

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(SpendJdbcExtension.class);

    private final Repository repository = new RepositoryJdbc();

    /*@Override
    public void beforeEach(ExtensionContext extensionContext) {
        AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                Spend.class
        ).ifPresent(
                spend -> {
                    SpendEntity tempSpend = SpendEntity.fromSpend(spend);
                    tempSpend = repository.createSpend(tempSpend);

                    extensionContext.getStore(NAMESPACE).put(
                            extensionContext.getUniqueId(), SpendJson.fromEntity(tempSpend)
                    );
                }
        );
    }*/

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