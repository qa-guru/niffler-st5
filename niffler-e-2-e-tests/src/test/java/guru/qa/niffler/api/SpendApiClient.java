package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

public class SpendApiClient extends ApiClient {

    private final SpendApi spendApi;

    public SpendApiClient() {
        super(CFG.spendUrl());
        this.spendApi = retrofit.create(SpendApi.class);
    }

    public SpendJson createSpend(SpendJson spendJson) throws Exception {
        return spendApi.createSpend(spendJson)
                .execute()
                .body();
    }

    public CategoryJson createCategory(CategoryJson categoryJson) throws Exception {
        return spendApi.createCategory(categoryJson)
                .execute()
                .body();
    }
}
