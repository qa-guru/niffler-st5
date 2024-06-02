package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryHibernate implements UserRepository{

    @Override
    public UserAuthEntity createUserInAuth(UserAuthEntity category) {
        return null;
    }

    @Override
    public UserEntity createUserInUserData(UserEntity category) {
        return null;
    }

    @Override
    public Optional<UserEntity> findUserInUserDataById(UUID id) {
        return Optional.empty();
    }
}
