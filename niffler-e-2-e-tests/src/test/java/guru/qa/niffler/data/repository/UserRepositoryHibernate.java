package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.jpa.EmProvider;
import jakarta.persistence.EntityManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

public class UserRepositoryHibernate implements UserRepository {

    private final EntityManager authEm = EmProvider.entityManager(DataBase.AUTH);
    private final EntityManager udEm = EmProvider.entityManager(DataBase.USERDATA);
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public UserAuthEntity createUserInAuth(UserAuthEntity user) {
        user.setPassword(pe.encode(user.getPassword()));
        authEm.persist(user);
        return user;
    }

    @Override
    public UserEntity createUserInUserdata(UserEntity user) {
        udEm.persist(user);
        return user;
    }

    @Override
    public Optional<UserEntity> findUserInUserdataById(UUID id) {
        return Optional.ofNullable(udEm.find(UserEntity.class, id));
    }
}
