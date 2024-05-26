package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserAuthEntity {
	private UUID id;
	private String username;
	private String password;
	private Boolean enabled;
	private Boolean accountNonExpired;
	private Boolean accountNonLocked;
	private Boolean credentialsNonExpired;
	private List<AuthorityEntity> authorities = new ArrayList<>();

	public UserAuthEntity testUserFromJson(UserJson userJson) {
		UserAuthEntity userAuthEntity = new UserAuthEntity();
		userAuthEntity.setUsername(userJson.username());
		userAuthEntity.setPassword(userJson.testData().password());
		userAuthEntity.setEnabled(true);
		userAuthEntity.setAccountNonExpired(true);
		userAuthEntity.setAccountNonLocked(true);
		userAuthEntity.setCredentialsNonExpired(true);
		return userAuthEntity;
	}
}
