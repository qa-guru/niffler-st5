package guru.qa.niffler.data.entity;

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


    public void setAuthorities(List<Authority> authorities) {
        this.authorities = new ArrayList<>();
        for (Authority a : authorities) {
            AuthorityEntity tae = new AuthorityEntity();
            tae.setAuthority(a);
            tae.setUser_id(this.id);
            this.authorities.add(tae);
        }
    }
}
