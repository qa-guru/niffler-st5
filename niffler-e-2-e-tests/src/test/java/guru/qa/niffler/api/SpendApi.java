package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface SpendApi {

    @POST("internal/spends/add")
    Call<SpendJson> createSpend(@Body SpendJson spendJson);

    @POST("internal/categories/add")
    Call<CategoryJson> createCategory(@Body CategoryJson spendJson);

    @GET("internal/categories/all")
    Call<List<CategoryJson>> getCategories(@Query("username") String username);

}
