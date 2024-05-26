package guru.qa.niffler.data.entity;

import guru.qa.niffler.enums.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserEntity {
	private UUID id;
	private String username;
	private CurrencyValues currency;
	private String firstname;
	private String surname;
	private byte[] photo;
	private byte[] photoSmall;

	public UserEntity fromUserJson(UserJson userJson) {
		UserEntity user = new UserEntity();
		user.setUsername(userJson.username());
		user.setCurrency(userJson.currency());
		user.setFirstname(userJson.firstname());
		user.setSurname(userJson.surname());
		return user;
	}
}