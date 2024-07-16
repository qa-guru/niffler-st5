package guru.qa.niffler.test.gql;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.model.gql.UserDataGql;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class UsersGqlTest extends BaseGqlTest {

    @Test
    @ApiLogin(user = @TestUser)
    void currentUserShouldBeReturned(@Token String bearerToken) throws Exception {
        JsonNode request;
        try (InputStream resource = this.getClass().getClassLoader().getResourceAsStream("gql/currentUserQuery.json")) {
            request = OM.readTree(resource);
        }

        UserDataGql currentUser = gatewayGqlApiClient.currentUser(bearerToken, request);
        Assertions.assertNotNull(currentUser.getUser().getUsername());
    }
}
