package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CategoryApi {

    @POST("internal/categories/add")
    Call<CategoryJson> createCategory(@Body CategoryJson categoryJson);
}
