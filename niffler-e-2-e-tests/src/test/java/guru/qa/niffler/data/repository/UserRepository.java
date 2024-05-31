package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.enums.Authority;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

	static UserRepository getInstance() {
		if ("sjdbc".equals(System.getProperty("repo"))) {
			return new UserRepositoryStringJdbc();
		}
		if ("hibernate".equals(System.getProperty("repo"))) {
			return new UserRepositoryHibernate();
		}
		return new UserRepositoryJdbc();
	}

	UserAuthEntity createUserInAuth(UserAuthEntity userAuthEntity);

	UserEntity createUserInUserdata(UserEntity userEntity);

	UserAuthEntity updateUserInAuth(UserAuthEntity userAuthEntity, List<Authority> listAuthority);

	UserEntity updateUserInUserdata(UserEntity userEntity);

	Optional<UserEntity> findUserInUserDataByID(UUID id);

	Optional<UserAuthEntity> findUserInAuthByUsername(String username);

	Optional<UserEntity> findInUserdataByUsername(String username);

}
