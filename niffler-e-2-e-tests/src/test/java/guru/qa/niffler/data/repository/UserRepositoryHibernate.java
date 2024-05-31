package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.jpa.EmProvider;
import guru.qa.niffler.enums.Authority;
import jakarta.persistence.EntityManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryHibernate implements UserRepository {

	private final EntityManager authEm = EmProvider.entityManager(DataBase.AUTH);
	private final EntityManager udEm = EmProvider.entityManager(DataBase.USERDATA);
	private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	@Override
	public UserAuthEntity createUserInAuth(UserAuthEntity userAuthEntity) {
		userAuthEntity.setPassword(pe.encode(userAuthEntity.getPassword()));
		authEm.persist(userAuthEntity);
		return userAuthEntity;
	}

	@Override
	public UserEntity createUserInUserdata(UserEntity userEntity) {
		udEm.persist(userEntity);
		return userEntity;
	}

	@Override
	public UserAuthEntity updateUserInAuth(UserAuthEntity userAuthEntity, List<Authority> listAuthority) {
		return authEm.merge(userAuthEntity);
	}

	@Override
	public UserEntity updateUserInUserdata(UserEntity userEntity) {
		return udEm.merge(userEntity);
	}

	@Override
	public Optional<UserEntity> findUserInUserDataByID(UUID id) {
		return Optional.ofNullable(udEm.find(UserEntity.class, id));
	}

	@Override
	public Optional<UserAuthEntity> findUserInAuthByUsername(String username) {
		return Optional.ofNullable(authEm.find(UserAuthEntity.class, username));
	}

	@Override
	public Optional<UserEntity> findInUserdataByUsername(String username) {
		return Optional.ofNullable(udEm.find(UserEntity.class, username));
	}
}
