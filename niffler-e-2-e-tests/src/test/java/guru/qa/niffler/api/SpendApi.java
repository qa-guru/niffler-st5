package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SpendApi {
    @POST("internal/spends/add")
    Call<SpendJson> createSpend(@Body SpendJson spendJson);

    @POST("internal/categories/add")
    Call<CategoryJson> addCategory(@Body CategoryJson spendJson);
}
