package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    static UserRepository getInstance() {
        String repo = System.getProperty("repo");
        if ("sjdbc".equals(repo)) {
            return new UserRepositorySpringJdbc();
        }
        if ("jdbc".equals(repo)) {
            return new UserRepositorySpringJdbc();
        }
        return new UserRepositoryHibernate();
    }

    UserAuthEntity createUserInAuth(UserAuthEntity User);

    UserEntity createUserInUserdata(UserEntity User);

    UserAuthEntity updateUserInAuth(UserAuthEntity User);

    UserEntity updateUserInUserdata(UserEntity User);

    Optional<UserEntity> findUserInUserdataById(UUID id);

    Optional<UserEntity> findUserInUserdataByUsername(String username);

    Optional<UserAuthEntity> findUserInAuthByUsername(String username);
}
