package guru.qa.niffler.retrofit.categoriesEndpoint;

import guru.qa.niffler.model.fromServer.CategoryResponse;
import guru.qa.niffler.model.toServer.CategoryBody;
import guru.qa.niffler.retrofit.RetrofitInitializer;
import org.junit.jupiter.api.Test;

public class CategoriesClient {

    private RetrofitInitializer retrofitInitializer;

    public CategoriesClient() {
        retrofitInitializer = new RetrofitInitializer("http://127.0.0.1:8093/internal/");
    }

    private CategoryBody createCategoryBody(String category, String username) {
        return new CategoryBody(null, category, username);
    }

    public CategoryResponse addNewCategory (String category, String username) {
        CategoriesService categoriesService = retrofitInitializer.createService(CategoriesService.class);
        return retrofitInitializer.executeRequest(categoriesService.addCategory(createCategoryBody(category, username)), CategoryResponse.class);
    }

//    @Test
//    void test() {
//        addNewCategory();
//    }

}
