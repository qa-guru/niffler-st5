package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.AuthorityEntity;
import guru.qa.niffler.data.entity.CurrencyValues;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryJdbc implements UserRepository {

    // Static fields для доступа к источникам данных и кодеру паролей
    private static final DataSource authDataSource = DataSourceProvider.dataSource(DataBase.AUTH);
    private static final DataSource userDataDataSource = DataSourceProvider.dataSource(DataBase.USERDATA);
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    // Метод для создания пользователя в базе данных аутентификации "niffler-auth"
    @Override
    public UserAuthEntity createUserInAuth(UserAuthEntity user) {
        try (Connection conn = authDataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (
                    PreparedStatement userPs = conn.prepareStatement(
                            """
                                    INSERT INTO "user" (
                                        username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)
                                    VALUES (?, ?, ?, ?, ?, ?)
                                    """,
                            //Флаг PreparedStatement.RETURN_GENERATED_KEYS указывает, что необходимо возвращать сгенерированные ключи
                            //(например, auto-increment id) после выполнения запроса вставки
                            PreparedStatement.RETURN_GENERATED_KEYS
                    );
                    PreparedStatement authorityPs = conn.prepareStatement(
                            """
                                    INSERT INTO "authority" (
                                        user_id, authority)
                                    VALUES (?, ?)
                                    """
                    )) {

                // Установка имени пользователя, пароля и других атрибутов
                userPs.setString(1, user.getUsername());
                userPs.setString(2, pe.encode(user.getPassword()));
                userPs.setBoolean(3, user.getEnabled());
                userPs.setBoolean(4, user.getAccountNonExpired());
                userPs.setBoolean(5, user.getAccountNonLocked());
                userPs.setBoolean(6, user.getCredentialsNonExpired());
                userPs.executeUpdate();

                // Получение сгенерированного идентификатора пользователя
                UUID generatedUserId = null;
                try (ResultSet resultSet = userPs.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedUserId = UUID.fromString(resultSet.getString("id"));
                    } else {
                        throw new IllegalStateException("Не удалось получить идентификатор");
                    }
                }

                user.setId(generatedUserId);

                // Вставка полномочий пользователя
                for (AuthorityEntity authority : user.getAuthorities()) {
                    authorityPs.setObject(1, user.getId());
                    authorityPs.setString(2, authority.getAuthority().name());
                    authorityPs.addBatch();
                    authorityPs.clearParameters();
                }

                authorityPs.executeBatch();

                // Фиксация изменений
                conn.commit();
                return user;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Метод для создания пользователя в базе данных 'niffler-userdata'
    @Override
    public UserEntity createUserInUserData(UserEntity user) {
        try (Connection conn = userDataDataSource.getConnection();
             PreparedStatement userPs = conn.prepareStatement(
                     """
                             INSERT INTO "user" (
                                 username, currency, firstname, surname, photo, photo_small)
                             VALUES (?, ?, ?, ?, ?, ?)
                             """,
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            userPs.setString(1, user.getUsername());
            userPs.setString(2, user.getCurrency().name());
            userPs.setString(3, user.getFirstname());
            userPs.setString(4, user.getSurname());
            userPs.setObject(5, user.getPhoto());
            userPs.setObject(6, user.getPhotoSmall());
            userPs.executeUpdate();

            UUID generatedUserId = null;
            try (ResultSet resultSet = userPs.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedUserId = UUID.fromString(resultSet.getString("id"));
                } else {
                    throw new IllegalStateException("Can`t access to id");
                }
            }
            user.setId(generatedUserId);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Метод для поиска пользователя в базе данных аутентификации
    @Override
    public Optional<UserAuthEntity> findUserInAuth(String userName) {
        UserAuthEntity userAuthEntity = new UserAuthEntity();

        try (Connection connection = authDataSource.getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(
                     """
                             SELECT * FROM "user" where  username = ?
                             """,
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            prepareStatement.setObject(1, userName);
            prepareStatement.execute();
            try (ResultSet resultSet = prepareStatement.getResultSet()) {
                if (resultSet.next()) {
                    userAuthEntity.setId((UUID) resultSet.getObject("id"));
                    userAuthEntity.setUsername(resultSet.getString("username"));
                    userAuthEntity.setPassword(resultSet.getString("password"));
                    userAuthEntity.setEnabled(resultSet.getBoolean("enabled"));
                    userAuthEntity.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
                    userAuthEntity.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
                    userAuthEntity.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return Optional.of(userAuthEntity);
    }

    // Обновляет существующего пользователя в базе данных аутентификации.
    // Возвращает обновленного пользователя UserAuthEntity
    @Override
    public UserAuthEntity updateUserInAuth(UserAuthEntity user) {
        try (Connection connection = authDataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (
                    PreparedStatement userPs = connection.prepareStatement(
                            """
                                    UPDATE "user"
                                    SET id=?, username=?, password=?, enabled=?, account_non_expired=?, account_non_locked=?, credentials_non_expired=?
                                    WHERE id =?
                                    """
                    );
                    PreparedStatement deleteAuthorityPs = connection.prepareStatement(
                            """
                                    DELETE FROM "authority" WHERE user_id = ?
                                    """
                    );
                    PreparedStatement authPs = connection.prepareStatement(
                            """
                                    INSERT INTO authority (user_id, authority)
                                    VAlUES (?, ?)
                                    """
                    )) {

                userPs.setObject(1, user.getId());
                userPs.setString(2, user.getUsername());
                userPs.setString(3, pe.encode(user.getPassword()));
                userPs.setObject(4, user.getEnabled());
                userPs.setObject(5, user.getAccountNonExpired());
                userPs.setObject(6, user.getAccountNonLocked());
                userPs.setObject(7, user.getCredentialsNonExpired());
                userPs.setObject(8, user.getId());
                userPs.executeUpdate();

                deleteAuthorityPs.setObject(1, user.getId());
                deleteAuthorityPs.executeUpdate();

                for (AuthorityEntity authority : user.getAuthorities()) {
                    authPs.setObject(1, user.getId());
                    authPs.setString(2, authority.getAuthority().name());
                    authPs.addBatch();
                    authPs.clearParameters();
                }

                authPs.executeBatch();
                connection.commit();
                return user;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Метод для обновления пользователя в базе данных 'niffler-userdata'
    @Override
    public UserEntity updateUserInUserdata(UserEntity user) {
        try (Connection connection = userDataDataSource.getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(
                     """
                             UPDATE "user"
                             SET username=?, currency=?, firstname=?, surname=?, photo=?, photo_small=?
                             WHERE id=?
                             """
             )) {

            prepareStatement.setString(1, user.getUsername());
            prepareStatement.setString(2, user.getCurrency().name());
            prepareStatement.setString(3, user.getFirstname());
            prepareStatement.setString(4, user.getSurname());
            prepareStatement.setObject(5, user.getPhoto());
            prepareStatement.setObject(6, user.getPhotoSmall());
            prepareStatement.setObject(7, user.getId());

            prepareStatement.executeUpdate();

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Находит пользователя по его идентификатору в базе данных 'niffler-userdata'
    @Override
    public Optional<UserEntity> findUserInUserDataById(UUID id) {
        UserEntity userEntity = new UserEntity();

        try (Connection connection = userDataDataSource.getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(
                     """
                             SELECT * FROM "user" where  id= ?
                             """
             )) {

            prepareStatement.setObject(1, id);
            prepareStatement.execute();

            try (ResultSet resultSet = prepareStatement.getResultSet()) {

                if (resultSet.next()) {
                    userEntity.setId((UUID) resultSet.getObject("id"));
                    userEntity.setUsername(resultSet.getString("username"));
                    userEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                    userEntity.setFirstname(resultSet.getString("firstname"));
                    userEntity.setSurname(resultSet.getString("surname"));
                    userEntity.setPhoto(resultSet.getBytes("photo"));
                } else {
                    return Optional.empty();
                }

            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return Optional.of(userEntity);
    }

    // Метод для поиска пользователя по имени в базе данных 'niffler-userdata'
    @Override
    public Optional<Object> findUserInUserData(String userName) {
        UserEntity userEntity = new UserEntity();

        try (Connection connection = userDataDataSource.getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(
                     """
                             SELECT * FROM "user" where  username = ?
                             """
             )) {

            prepareStatement.setObject(1, userName);
            prepareStatement.execute();

            try (ResultSet resultSet = prepareStatement.getResultSet()) {

                if (resultSet.next()) {
                    userEntity.setId((UUID) resultSet.getObject("id"));
                    userEntity.setUsername(resultSet.getString("username"));
                    userEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                    userEntity.setFirstname(resultSet.getString("firstname"));
                    userEntity.setSurname(resultSet.getString("surname"));
                    userEntity.setPhoto(resultSet.getBytes("photo"));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return Optional.of(userEntity);
    }
}
