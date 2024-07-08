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

    // Создаем экземпляры EntityManager для авторизации и пользовательских данных
    private final EntityManager authEm = EmProvider.entityManager(DataBase.AUTH);
    private final EntityManager udEm = EmProvider.entityManager(DataBase.USERDATA);

    // Создаем экземпляр PasswordEncoder для шифрования паролей
    private static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    // Создаем пользователя в системе авторизации
    public UserAuthEntity createUserInAuth(UserAuthEntity user) {
        // Шифруем пароль пользователя
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.getAuthorities().get(0).setUser(user);
        user.getAuthorities().get(1).setUser(user);
        // Persist - сохраняем пользователя в базе данных авторизации
        authEm.persist(user);
        // Возвращаем созданного пользователя
        return user;
    }

    @Override
    // Создаем пользователя в системе пользовательских данных
    public UserEntity createUserInUserData(UserEntity user) {
        // Persist - сохраняем пользователя в базе данных пользовательских данных
        udEm.persist(user);
        // Возвращаем созданного пользователя
        return user;
    }

    @Override
    // Нахождение пользователя в системе авторизации по имени
    public Optional<UserAuthEntity> findUserInAuth(String userName) {
        // Создаем запрос к базе данных авторизации
        return Optional.ofNullable(authEm.createQuery("FROM UserAuthEntity WHERE username = :username", UserAuthEntity.class)
                // Устанавливаем параметр запроса
                .setParameter("username", userName)
                // Получаем результат запроса
                .getSingleResult());
    }

    @Override
    // Нахождение пользователя в системе пользовательских данных по идентификатору
    public Optional<UserEntity> findUserInUserDataById(UUID id) {
        // Используем метод find для поиска пользователя по идентификатору
        return Optional.ofNullable(udEm.find(UserEntity.class, id));
    }

    @Override
    // Нахождение пользователя в системе пользовательских данных по имени
    public Optional<Object> findUserInUserData(String userName) {
        // Создаем запрос к базе данных пользовательских данных
        return Optional.ofNullable(udEm.createQuery("FROM UserEntity WHERE username = :username", UserEntity.class)
                // Устанавливаем параметр запроса
                .setParameter("username", userName)
                // Получаем результат запроса
                .getSingleResult());
    }

    @Override
    // Обновление пользователя в системе авторизации
    public UserAuthEntity updateUserInAuth(UserAuthEntity user) {
        return authEm.merge(user);
    }

    @Override
    // Обновление пользователя в системе пользовательских данных
    public UserEntity updateUserInUserdata(UserEntity user) {
        return udEm.merge(user);
    }
}
