package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.model.gql.UserDataGql;
import io.qameta.allure.Step;

import javax.annotation.Nullable;

public class GatewayGqlApiClient extends ApiClient {

    private final GatewayGqlApi gatewayGqlApi;

    public GatewayGqlApiClient() {
        super(CFG.gatewayUrl());
        this.gatewayGqlApi = retrofit.create(GatewayGqlApi.class);
    }

    @Step("Send GQL POST ('/graphql') request to niffler-gateway")
    @Nullable
    public UserDataGql currentUser(String bearerToken, JsonNode request) throws Exception {
        return gatewayGqlApi.currentUser(bearerToken, request)
                .execute()
                .body();
    }
}
