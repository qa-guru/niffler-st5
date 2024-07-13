package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;
import guru.qa.niffler.data.entity.UserEntity;
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
        byte[] photo,

        @JsonProperty("photoSmall")
        byte[] photoSmall,

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

    public static UserJson fromEntity(UserEntity userEntity, String password) {
        return new UserJson(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getFirstname(),
                userEntity.getSurname(),
                userEntity.getCurrency(),
                userEntity.getPhoto(),
                userEntity.getPhotoSmall(),
                null,
                new TestData(password)
        );
    }

    public static UserJson randomUser() {
        Faker faker = new Faker();

        return new UserJson(
                null,
                faker.name().username(),
                faker.name().firstName(),
                faker.name().lastName(),
                CurrencyValues.KZT,
                null,
                null,
                null,
                new TestData("generated_user")
        );
    }
}
