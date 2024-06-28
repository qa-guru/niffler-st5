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
        userAuthEntity.setId(userJson.id());
        userAuthEntity.setUsername(userJson.username());
        userAuthEntity.setPassword(userJson.testData().password());
        userAuthEntity.setEnabled(true);
        userAuthEntity.setAccountNonExpired(true);
        userAuthEntity.setAccountNonLocked(true);
        userAuthEntity.setCredentialsNonExpired(true);

        AuthorityEntity read = new AuthorityEntity();
        read.setAuthority(Authority.read);
        AuthorityEntity write = new AuthorityEntity();
        write.setAuthority(Authority.write);
        userAuthEntity.setAuthorities(List.of(read, write));
        return userAuthEntity;
    }
}
