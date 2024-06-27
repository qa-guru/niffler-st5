package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Step;
import lombok.SneakyThrows;

import java.util.Arrays;

public class SpendApiClient extends ApiClient {

    private final SpendApi spendApi;

    public SpendApiClient() {
        super(CFG.spendUrl());
        this.spendApi = retrofit.create(SpendApi.class);
    }

    @Step("API: создание расхода")
    @SneakyThrows
    public SpendJson createSpend(SpendJson spendJson) {
        return spendApi.createSpend(spendJson)
                .execute()
                .body();
    }

    @Step("API: создание категории")
    @SneakyThrows
    public CategoryJson createCategory(CategoryJson categoryJson) {
        return spendApi.createCategory(categoryJson)
                .execute()
                .body();
    }

    public void removeSpends(String username, String... ids) throws Exception {
        spendApi.removeSpends(username, Arrays.stream(ids).toList())
                .execute();
    }

}
