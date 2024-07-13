package guru.qa.niffler.jupiter.extension.spend;

import guru.qa.niffler.api.spend.SpendApiClient;
import guru.qa.niffler.model.SpendJson;

import java.io.IOException;
import java.util.List;

public class HttpSpendExtension extends AbstractSpendExtension {

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    protected SpendJson createSpend(SpendJson spendJson) {
        try {
            return spendApiClient.createSpend(spendJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void removeSpend(SpendJson spendJson) {
        try {
            spendApiClient.deleteSpends(spendJson.username(), List.of(spendJson.id()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}