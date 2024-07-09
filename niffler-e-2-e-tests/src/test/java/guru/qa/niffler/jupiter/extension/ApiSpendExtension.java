package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.SpendJson;

import java.util.List;

public class ApiSpendExtension extends AbstractSpendExtension {
    private final SpendApiClient spendApiClient = new SpendApiClient();
    @Override
    protected SpendJson createSpend(SpendJson spend) throws Exception {
        return spendApiClient.createSpend(spend);
    }

    @Override
    protected void removeSpend(SpendJson json) {
        spendApiClient.removeSpend(List.of(String.valueOf(json.id())));
    }
}
