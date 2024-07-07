package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;

import java.util.List;

public class SpendApiClient extends ApiClient {

    private final SpendApi spendApi;

    public SpendApiClient(){
        super(CFG.spendUrl());
        this.spendApi = retrofit.create(SpendApi.class);
    }

    public SpendJson createSpend(SpendJson spendJson) throws Exception{
        return spendApi.createSpend(spendJson)
                .execute()
                .body();
    }
    public SpendJson createSpend(SpendJson spendJson, CategoryJson category) throws Exception{
        return spendApi.createSpend(spendJson)
                .execute()
                .body();
    }


    public CategoryJson createCategory(CategoryJson spendJson) throws Exception{
        return spendApi.createCategory(spendJson)
                .execute()
                .body();
    }


    public Call<SpendJson> removeSpend(List<String> ids) {
        return null;
    }


    public Call<CategoryJson> removeCategory(List<String> ids) {
        return null;
    }
}
