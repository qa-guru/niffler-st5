package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CurrencyValues;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.data.repository.UserRepositoryJdbc;
import guru.qa.niffler.model.UserJson;

import java.util.UUID;

public class DbCreateUserExtension extends CreateUserExtension {

    UserRepository userRepository = new UserRepositoryJdbc();

    public UserJson createUser(UserJson user) {
        UserAuthEntity tempUserAuthEntity = new UserAuthEntity();
        tempUserAuthEntity.setUsername(user.username());
        tempUserAuthEntity.setPassword(user.testData().password());
        tempUserAuthEntity.setEnabled(true);
        tempUserAuthEntity.setAccountNonExpired(true);
        tempUserAuthEntity.setAccountNonLocked(true);
        tempUserAuthEntity.setCredentialsNonExpired(true);
        UUID id = userRepository.createUserInAuth(tempUserAuthEntity).getId();

        userRepository.createUserInAuth(tempUserAuthEntity);
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.username());
        userEntity.setCurrency(CurrencyValues.valueOf(String.valueOf(user.currency())));
        userRepository.createUserInUserdata(userEntity);
        return UserJson.setId(user, id);
    }
}