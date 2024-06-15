package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.model.UserJson;

public class DbCreateUserExtension extends AbstractCreateUserExtension {
    private final ThreadLocal<UserRepository> userRepositoryThreadLocal = new ThreadLocal<>();

    @Override
    protected UserJson createUser(UserJson user) {
        getThreadLocalRepoInstance().createUserInAuth(UserAuthEntity.fromJson(user));
        getThreadLocalRepoInstance().createUserInUserData(UserEntity.fromJson(user));
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
