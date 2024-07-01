package guru.qa.niffler.util;

import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.data.repository.UserRepositorySpringJdbc;
import guru.qa.niffler.jupiter.extension.AbstractCreateUserExtension;
import guru.qa.niffler.model.UserJson;

public class DbCreateUserExtension extends AbstractCreateUserExtension {

    UserRepository userRepository = new UserRepositorySpringJdbc();

    @Override
    public UserJson createUser(UserJson user) {
        userRepository.createUserInAuth(UserAuthEntity.fromJson(user));
        userRepository.createUserInUserdata(UserEntity.fromJson(user));
        return user;
    }
}