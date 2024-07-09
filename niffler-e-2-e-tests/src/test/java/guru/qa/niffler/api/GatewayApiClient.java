package guru.qa.niffler.api;

import guru.qa.niffler.model.*;
import retrofit2.Call;

import java.util.List;

public class GatewayApiClient extends ApiClient {
    private final GatewayApi gatewayApi;

    public GatewayApiClient() {
        super(CFG.spendUrl());
        this.gatewayApi = retrofit.create(GatewayApi.class);
    }

    public Call<List<SpendJson>> getSpends(String bearerToken) {
        return gatewayApi.getSpends(bearerToken);
    }

    public Call<List<CategoryJson>> getCategories(String bearerToken) {
        return gatewayApi.getCategories(bearerToken);
    }

    public Call<CategoryJson> addCategory(String bearerToken, CategoryJson categoryJson) {
        return gatewayApi.addCategory(bearerToken, categoryJson);
    }

    public Call<List<CurrencyJson>> getAllCurrencies(String bearerToken) {
        return gatewayApi.getAllCurrencies(bearerToken);
    }

    public Call<UserJson> currentUser(String bearerToken) {
        return gatewayApi.currentUser(bearerToken);
    }

    public Call<List<UserJson>> friends(String bearerToken) {
        return gatewayApi.friends(bearerToken);
    }

    public void removeFriend(String bearerToken, String username) {
        gatewayApi.removeFriend(bearerToken, username);
    }

    public Call<List<UserJson>> incomeInvitations(String bearerToken) {
        return gatewayApi.incomeInvitations(bearerToken);
    }

    public Call<List<UserJson>> outcomeInvitations(String bearerToken) {
        return gatewayApi.outcomeInvitations(bearerToken);
    }

    public Call<UserJson> sendInvitation(String bearerToken, FriendJson friend) {
        return gatewayApi.sendInvitation(bearerToken, friend);
    }

    public Call<UserJson> acceptInvitation(String bearerToken, FriendJson friend) {
        return gatewayApi.acceptInvitation(bearerToken, friend);
    }

    public Call<UserJson> declineInvitation(String bearerToken, FriendJson friend) {
        return gatewayApi.declineInvitation(bearerToken, friend);
    }

    public Call<List<UserJson>> allUsers(String bearerToken) {
        return gatewayApi.allUsers(bearerToken);
    }

    public Call<SpendJson> addSpend(String bearerToken, SpendJson spendJson) {
        return gatewayApi.addSpend(bearerToken, spendJson);
    }

    public void deleteSpends(String bearerToken, List<String> ids) {
        gatewayApi.deleteSpends(bearerToken, ids);
    }

    public Call<SpendJson> editSpend(String bearerToken, SpendJson spendJson) {
        return gatewayApi.editSpend(bearerToken, spendJson);
    }

    public Call<UserJson> updateUserInfo(String bearerToken, UserJson userJson) {
        return gatewayApi.updateUserInfo(bearerToken, userJson);
    }
}
