package guru.qa.niffler.retrofit.spendsEndpoint;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.retrofit.RetrofitInitializer;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

public class SpendsClient {

    private RetrofitInitializer retrofitInitializer;

    public SpendsClient() {
        retrofitInitializer = new RetrofitInitializer("http://127.0.0.1:8093/internal/");
    }

    private SpendJson createSpendsBody(String category, CurrencyValues currencyValues, Double amount, String description, String username) {
        return new SpendJson(null, new Date(), category, currencyValues, amount, description, username);
    }

    public SpendJson createSpend(String category, CurrencyValues currencyValues, Double amount, String description, String username) {
        SpendsService spendsService = retrofitInitializer.createService(SpendsService.class);
        return retrofitInitializer.executeRequest(spendsService.createSpend(createSpendsBody(category, currencyValues, amount, description, username)), SpendJson.class);
    }

    @Test
    void test() {
        createSpend();
    }

}
