package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

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

    public static UserEntity fromJson(UserJson userJson) {
        UserEntity entity = new UserEntity();
        entity.setId(userJson.id());
        entity.setUsername(userJson.username());
        entity.setCurrency(userJson.currency());
        entity.setFirstname(userJson.firstname());
        entity.setSurname(userJson.surname());
        return entity;
    }

}