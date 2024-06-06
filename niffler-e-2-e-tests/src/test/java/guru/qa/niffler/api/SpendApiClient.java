package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import javax.annotation.Nonnull;
import java.util.Arrays;

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

    public void removeSpends(@Nonnull String username, @Nonnull String... ids) throws Exception {
        spendApi.removeSpends(username, Arrays.stream(ids).toList())
                .execute();
    }

    public CategoryJson createCategory(CategoryJson categoryJson) throws Exception {
        return spendApi.createCategory(categoryJson)
                .execute()
                .body();
    }
}
