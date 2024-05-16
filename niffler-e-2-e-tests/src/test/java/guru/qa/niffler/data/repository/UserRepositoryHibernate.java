package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public class UserRepositoryHibernate implements UserRepository {
    @Override
    public UserAuthEntity createUserInAuth(UserAuthEntity user) {
        return null;
    }

    @Override
    public UserEntity createUserInUserdata(UserEntity user) {
        return null;
    }

    @Override
    public Optional<UserEntity> findUserInUserdataById(UUID id) {
        return Optional.empty();
    }
}
