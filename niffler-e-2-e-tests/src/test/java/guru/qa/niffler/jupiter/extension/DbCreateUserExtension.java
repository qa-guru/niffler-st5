package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.model.UserJson;

import static guru.qa.niffler.data.repository.logging.AllureJsonAppender.attachJsonData;

public class DbCreateUserExtension extends AbstractCreateUserExtension {
    private final ThreadLocal<UserRepository> userRepositoryThreadLocal = new ThreadLocal<>();

    @Override
    protected UserJson createUser(UserJson user) {
        UserEntity userEntity = UserEntity.fromJson(user);
        UserAuthEntity userAuthEntity = UserAuthEntity.fromJson(user);

        attachJsonData("Created UserEntity", userEntity);
        attachJsonData("Created UserAuthEntity", userAuthEntity);

        getThreadLocalRepoInstance().createUserInAuth(userAuthEntity);
        getThreadLocalRepoInstance().createUserInUserData(userEntity);
        return user;
    }

    private UserRepository getThreadLocalRepoInstance() {
        UserRepository userRepository = userRepositoryThreadLocal.get();
        if (userRepository == null) {
            userRepository = UserRepository.getInstance();
            userRepositoryThreadLocal.set(userRepository);
        }
        return userRepository;
    }

}
