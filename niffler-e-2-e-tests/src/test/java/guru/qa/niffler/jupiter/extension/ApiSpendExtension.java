package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import lombok.SneakyThrows;

import static guru.qa.niffler.utils.DateHelper.convertStringToDate;

public class ApiSpendExtension extends AbstractSpendExtension {

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    protected SpendJson createSpend(Spend spend, CategoryJson category) {

        // Создаем объект расхода
        SpendJson spendJson = new SpendJson(
                null,
                // Конвертируем дату из строки в формат Date
                convertStringToDate(spend.spendDate()),
                spend.category(),
                spend.currency(),
                spend.amount(),
                spend.description(),
                spend.username()
        );

        return spendApiClient.createSpend(spendJson);
    }

    @Override
    @SneakyThrows
    protected SpendJson createSpend(SpendJson spend) {
        return spendApiClient.createSpend(spend);
    }

    @Override
    @SneakyThrows
    protected void removeSpend(SpendJson spend) {
        spendApiClient.removeSpends(spend.username(), String.valueOf(spend.id()));
    }

}
