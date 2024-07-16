package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.model.gql.UserDataGql;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GatewayGqlApi {

    @POST("graphql")
    Call<UserDataGql> currentUser(@Header("Authorization") String bearerToken,
                                  @Body JsonNode request);

}
