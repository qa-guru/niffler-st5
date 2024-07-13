package guru.qa.niffler.api.spend;

import guru.qa.niffler.api.ApiClient;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class SpendApiClient extends ApiClient {

    private final SpendApi spendApi;

    public SpendApiClient() {
        super(CONFIG.spendUrl());
        this.spendApi = retrofit.create(SpendApi.class);
    }

    public SpendJson createSpend(SpendJson spendJson) throws IOException {
        return spendApi.createSpend(spendJson)
                .execute().body();
    }

    public void deleteSpends(String username, List<UUID> ids) throws IOException {
        spendApi.deleteSpends(username, ids)
                .execute();
    }

    public CategoryJson createCategory(CategoryJson categoryJson) throws IOException {
        return spendApi.createCategory(categoryJson)
                .execute().body();
    }

    public List<CategoryJson> getCategories(String username) throws IOException {
        return spendApi.getCategories(username)
                .execute().body();
    }

}
