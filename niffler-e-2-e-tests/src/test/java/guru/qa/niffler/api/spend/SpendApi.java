package guru.qa.niffler.api.spend;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;
import java.util.UUID;

public interface SpendApi {

    @POST("internal/spends/add")
    Call<SpendJson> createSpend(@Body SpendJson spendJson);

    @DELETE("internal/spends/remove")
    Call<Void> deleteSpends(@Query("username") String username, @Query("ids") List<UUID> ids);

    @POST("internal/categories/add")
    Call<CategoryJson> createCategory(@Body CategoryJson categoryJson);

    @GET("internal/categories/all")
    Call<List<CategoryJson>> getCategories(@Query("username") String username);

}