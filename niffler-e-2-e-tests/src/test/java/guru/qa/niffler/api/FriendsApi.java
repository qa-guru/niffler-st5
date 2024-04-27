package guru.qa.niffler.api;

import guru.qa.niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface FriendsApi {
    @POST("/internal/invitations/send")
    Call<UserJson> sendInvitation(@Field("username") UserJson userForTest,
                                  @Field("username") UserJson userForAnotherTest
    );

    @POST("/internal/invitations/accept")
    Call<UserJson> acceptInvitation(@Field("username") UserJson userForTest,
                                    @Field("username") UserJson userForAnotherTest
    );

    @POST("/internal/invitations/decline")
    Call<UserJson> declineInvitation(@Field("username") UserJson userForTest,
                                     @Field("username") UserJson userForAnotherTest
    );
}
