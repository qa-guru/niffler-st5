package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CategoriesApi {
    @POST("internal/categories/add")
    Call<CategoryJson> addCategory(@Body CategoryJson spendJson);
}
