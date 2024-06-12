package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;

// Указывает, что поля с null-значениями не должны включаться в JSON
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserJson(
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

    // Метод для создания простого объекта UserJson с указанным именем пользователя и паролем
    public static UserJson simpleUser(String username, String password) {
        return new UserJson(
                username,
                null,
                null,
                null,
                null,
                null,
                null,
                new TestData(
                        password
                )
        );
    }

    // Метод для создания случайного объекта UserJson
    public static UserJson randomUser() {

        // Создание объекта Faker для генерации случайных данных
        Faker faker = new Faker();

        // Генерация случайного имени пользователя
        String username = faker.name().username();

        // Генерация случайного имени пользователя (первое имя)
        String firstname = faker.name().firstName();

        // Генерация случайной фамилии пользователя
        String surname = faker.name().lastName();

        // Генерация случайной валюты из перечисления CurrencyValues
        CurrencyValues currency = faker.options().option(CurrencyValues.class);

        // Создание случайного объекта UserJson
        return new UserJson(
                username,
                firstname,
                surname,
                currency,
                null,
                null,
                null,
                new TestData(
                        faker.internet().password()
                )
        );
    }

}
