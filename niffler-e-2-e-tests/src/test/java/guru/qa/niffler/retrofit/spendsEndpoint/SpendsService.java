package guru.qa.niffler.retrofit.spendsEndpoint;

import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SpendsService {

    @POST("spends/add")
    Call<SpendJson> createSpend(@Body SpendJson spendJson);

}
