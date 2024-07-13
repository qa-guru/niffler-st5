package guru.qa.niffler.jupiter.extension.user;

import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.user.UserRepository;
import guru.qa.niffler.model.UserJson;

import static guru.qa.niffler.data.repository.RepositoryType.HIBERNATE;

public class DbCreateUserExtension extends CreateUserExtension {

    @Override
    UserJson createUser(UserJson user) {
        UserRepository userRepository = UserRepository.getInstance(HIBERNATE);

        UserEntity userEntity = UserEntity.fromJson(user);
        UserAuthEntity userAuthEntity = UserAuthEntity.fromJson(user);

        userRepository.createUserInAuth(userAuthEntity);
        userRepository.createUserInUserData(userEntity);

        return UserJson.fromEntity(userEntity, user.testData().password());
    }

}
