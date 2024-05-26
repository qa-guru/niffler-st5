package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.enums.Authority;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryStringHibernate implements UserRepository {
	@Override
	public UserAuthEntity createUserInAuth(UserAuthEntity userAuthEntity) {
		return null;
	}

	@Override
	public UserEntity createUserInUserdata(UserEntity userEntity) {
		return null;
	}

	@Override
	public UserAuthEntity updateUserInAuth(UserAuthEntity userAuthEntity, List<Authority> listAuthority) {
		return null;
	}

	@Override
	public UserEntity updateUserInUserdata(UserEntity userEntity) {
		return null;
	}

	@Override
	public Optional<UserEntity> findUserInUserDataByID(UUID id) {
		return Optional.empty();
	}
}
