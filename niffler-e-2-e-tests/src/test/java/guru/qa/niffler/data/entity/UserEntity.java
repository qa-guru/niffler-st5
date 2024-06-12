package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

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
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userEntity.getId());
        userEntity.setUsername(userJson.username());
        userEntity.setCurrency(userJson.currency());
        userEntity.setFirstname(userJson.firstname());
        userEntity.setSurname(userJson.surname());
        userEntity.setPhoto(userJson.photo());
        userEntity.setPhotoSmall(userJson.photoSmall());
        return userEntity;
    }

}