package guru.qa.niffler.api;

import guru.qa.niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FriendsApi {
    @POST("/internal/invitations/send")
    Call<UserJson> sendInvitation(@Query("username") String username,
                                  @Query("username") String targetUsername
    );

    @POST("/internal/invitations/accept")
    Call<UserJson> acceptInvitation(@Query("username") String username,
                                    @Query("username") String targetUsername
    );

    @POST("/internal/invitations/decline")
    Call<UserJson> declineInvitation(@Query("username") String username,
                                     @Query("username") String targetUsername
    );
}
