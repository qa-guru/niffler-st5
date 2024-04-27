package guru.qa.niffler.retrofit.categoriesEndpoint;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.retrofit.RetrofitInitializer;

public class CategoriesClient {

    private RetrofitInitializer retrofitInitializer;

    public CategoriesClient() {
        retrofitInitializer = new RetrofitInitializer("http://127.0.0.1:8093/internal/");
    }

    private CategoryJson createCategoryBody(String category, String username) {
        return new CategoryJson(null, category, username);
    }

    public CategoryJson addNewCategory (String category, String username) {
        CategoriesService categoriesService = retrofitInitializer.createService(CategoriesService.class);
        return retrofitInitializer.executeRequest(categoriesService.addCategory(createCategoryBody(category, username)), CategoryJson.class);
    }


}
