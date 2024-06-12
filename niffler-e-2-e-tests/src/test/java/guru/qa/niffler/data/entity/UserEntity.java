package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

// Класс сущности для пользователя
@Getter
@Setter
public class UserEntity implements Serializable {
    private UUID id;
    private String username;
    private CurrencyValues currency;
    private String firstname;
    private String surname;
    private byte[] photo;
    private byte[] photoSmall;

    // Метод для создания сущности UserEntity из JSON-объекта пользователя
    public static UserEntity fromJson(UserJson userJson) {
        // Создание сущности UserEntity
        UserEntity userEntity = new UserEntity();

        // Установка значений полей сущности
        userEntity.setUsername(userJson.username());
        userEntity.setCurrency(CurrencyValues.valueOf(userJson.currency().name()));
        userEntity.setFirstname(userJson.firstname());
        userEntity.setSurname(userJson.surname());

        return userEntity;
    }
}