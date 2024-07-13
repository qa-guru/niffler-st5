package guru.qa.niffler.data.repository.user;

import guru.qa.niffler.data.Database;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.jpa.EmProvider;
import jakarta.persistence.EntityManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

public class UserRepositoryHibernate implements UserRepository {

    private final EntityManager userDataEm = EmProvider.entityManager(Database.USERDATA);
    private final EntityManager authEm = EmProvider.entityManager(Database.AUTH);
    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public UserAuthEntity createUserInAuth(UserAuthEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        authEm.persist(user);
        return user;
    }

    @Override
    public UserEntity createUserInUserData(UserEntity user) {
        userDataEm.persist(user);
        return user;
    }

    @Override
    public UserAuthEntity findUserAuthByUsername(String username) {
        return authEm.createQuery(
                        "FROM UserAuthEntity WHERE username = :username",
                        UserAuthEntity.class
                )
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public UserEntity findUserInUserdataById(UUID id) {
        return userDataEm.createQuery(
                        "FROM UserEntity WHERE id = :id",
                        UserEntity.class
                )
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<UserEntity> findUserByUsername(String username) {
        return userDataEm.createQuery(
                        "FROM UserEntity WHERE username = :username",
                        UserEntity.class
                )
                .setParameter("username", username)
                .getResultList();
    }

    @Override
    public UserAuthEntity updateUserInAuth(UserAuthEntity user) {
        authEm.merge(user);
        return user;
    }

    @Override
    public UserEntity updateUserInUserdata(UserEntity user) {
        userDataEm.merge(user);
        return user;
    }

}