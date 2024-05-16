package guru.qa.niffler.data.entity;

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

}