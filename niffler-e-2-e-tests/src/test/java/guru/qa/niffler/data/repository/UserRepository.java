package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    static UserRepository getInstance() {
        if ("sjdbs".equals(System.getProperty("repo"))) {
            return new UserRepositorySpringJdbc();
        }
        return new UserRepositoryJdbc();
    }

    UserAuthEntity createUserInAuth(UserAuthEntity User);

    UserEntity createUserInUserdata(UserEntity User);

    Optional<UserEntity> findUserInUserdataById(UUID id);
}
