package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.UserRepositoryStringJdbc;
import guru.qa.niffler.model.UserJson;

import java.util.Arrays;

public class DbCreateUserExtension extends CreateUserExtension {

	private final UserRepositoryStringJdbc userRepositoryStringJdbc = new UserRepositoryStringJdbc();

	@Override
	UserJson createUser(UserJson user) {
		UserEntity userEntity = userRepositoryStringJdbc.createUserInUserdata(new UserEntity().fromUserJson(user));
		userRepositoryStringJdbc.createUserInAuth(new UserAuthEntity().testUserFromJson(user));

		return new UserJson(
				userEntity.getId(),
				userEntity.getUsername(),
				userEntity.getFirstname(),
				userEntity.getSurname(),
				userEntity.getCurrency(),
				Arrays.toString(userEntity.getPhoto()),
				Arrays.toString(userEntity.getPhotoSmall()),
				user.friendState(),
				user.testData()
		);
	}
}
