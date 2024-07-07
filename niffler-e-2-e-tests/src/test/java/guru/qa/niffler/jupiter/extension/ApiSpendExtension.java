package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.SpendJson;

public class ApiSpendExtension extends AbstractSpendExtension {
    private final SpendApiClient spendApiClient = new SpendApiClient();
    @Override
    protected SpendJson createSpend(SpendJson spend) throws Exception {
        return spendApiClient.createSpend(spend);
    }

    @Override
    protected void removeSpend(SpendJson json) {

    }
}
