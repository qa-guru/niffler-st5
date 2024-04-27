package guru.qa.niffler.retrofit.categoriesEndpoint;

import guru.qa.niffler.model.CategoryJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface CategoriesService {

    @POST("categories/add")
    Call<CategoryJson> addCategory (@Body CategoryJson categoryBody);

}
