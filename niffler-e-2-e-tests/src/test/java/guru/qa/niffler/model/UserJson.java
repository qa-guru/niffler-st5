package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.userdata.wsdl.FriendState;

import java.util.UUID;

public record UserJson(
        @JsonProperty("id")
        UUID id,

        @JsonProperty("username")
        String username,

        @JsonProperty("firstname")
        String firstname,

        @JsonProperty("surname")
        String surname,

        @JsonProperty("currency")
        CurrencyValues currency,

        @JsonProperty("photo")
        String photo,

        @JsonProperty("photoSmall")
        String photoSmall,

        @JsonProperty("friendState")
        FriendState friendState,

        @JsonIgnore
        TestData testData) {

    public static UserJson simpleUser(String username, String password) {
        return new UserJson(
                null,
                username,
                null,
                null,
                null,
                null,
                null,
                null,
                new TestData(password)
        );
    }
}
