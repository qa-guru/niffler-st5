package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserAuthEntity implements Serializable {

    private UUID id;
    private String username;
    private String password;
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private List<AuthorityEntity> authorities = new ArrayList<>();

    public static UserAuthEntity fromJson(UserJson userJson) {
        UserAuthEntity userAuthEntity = new UserAuthEntity();
        AuthorityEntity readAuthority = new AuthorityEntity();
        AuthorityEntity writeAuthority = new AuthorityEntity();

        readAuthority.setUser_id(userJson.id());
        readAuthority.setAuthority(Authority.read);
        writeAuthority.setUser_id(userJson.id());
        writeAuthority.setAuthority(Authority.write);

        userAuthEntity.setId(userJson.id());
        userAuthEntity.setUsername(userJson.username());
        userAuthEntity.setPassword(userJson.testData().password());
        userAuthEntity.setEnabled(true);
        userAuthEntity.setAccountNonExpired(true);
        userAuthEntity.setAccountNonLocked(true);
        userAuthEntity.setCredentialsNonExpired(true);
        userAuthEntity.setAuthorities(List.of(readAuthority, writeAuthority));
        return userAuthEntity;
    }

}