package guru.qa.niffler.data.repository.user;

import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.RepositoryType;

import java.util.List;
import java.util.UUID;


public interface UserRepository {

    static UserRepository getInstance(RepositoryType type) {
        switch (type) {
            case JDBC -> {
                return new UserRepositoryJdbc();
            }
            case SPRING_JDBC -> {
                return new UserRepositorySpringJdbc();
            }
            case HIBERNATE -> {
                return new UserRepositoryHibernate();
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    UserAuthEntity createUserInAuth(UserAuthEntity user);

    UserEntity createUserInUserData(UserEntity user);

    UserAuthEntity findUserAuthByUsername(String username);

    UserEntity findUserInUserdataById(UUID id);

    List<UserEntity> findUserByUsername(String username);

    UserAuthEntity updateUserInAuth(UserAuthEntity user);

    UserEntity updateUserInUserdata(UserEntity user);

}