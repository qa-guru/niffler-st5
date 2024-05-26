package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SpendApi {
    @POST("internal/spends/add")
    Call<SpendJson> createSpend(@Body SpendJson spendJson);

    @POST("internal/categories/add")
    Call<CategoryJson> createCategory(@Body CategoryJson spendJson);

    @DELETE("internal/spends/remove")
    Call<SpendJson> removeSpend(@Query("ids") List<String> ids);

    @DELETE("internal/categories/remove")
    Call<CategoryJson> removeCategory(@Query("ids") List<String> ids);


}
