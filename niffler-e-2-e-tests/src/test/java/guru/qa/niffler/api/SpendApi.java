package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

/**
 * Интерфейс для API-интерфейса, который обеспечивает взаимодействие с API для добавления расходов и категорий.
 */
public interface SpendApi {

    /**
     * Метод для создания расхода.
     *
     * @param spendJson Объект, содержащий информацию о расходе.
     * @return Объект, содержащий информацию о созданном расходе.
     */
    @POST("internal/spends/add")
    Call<SpendJson> createSpend(@Body SpendJson spendJson);

    @DELETE("internal/spends/remove")
    Call<Void> removeSpends(@Query("username") String username, @Query("ids") List<String> ids);


    /**
     * Метод для создания категории.
     *
     * @param spendJson Объект, содержащий информацию о категории.
     * @return Объект, содержащий информацию о созданной категории.
     */
    @POST("internal/categories/add")
    Call<CategoryJson> createCategory(@Body CategoryJson spendJson);
}
