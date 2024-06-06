package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.SpendJson;
import lombok.SneakyThrows;

public class ApiSpendExtension extends SpendExtension {

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    @SneakyThrows
    protected SpendJson createSpend(SpendJson spend) {
        return spendApiClient.createSpend(spend);
    }

    @Override
    @SneakyThrows
    protected void removeSpend(SpendJson spend) {
        spendApiClient.removeSpends(
                spend.username(),
                spend.id().toString()
        );
    }
}
