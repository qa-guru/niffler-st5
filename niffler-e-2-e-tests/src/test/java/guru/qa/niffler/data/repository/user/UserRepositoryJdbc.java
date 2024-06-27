package guru.qa.niffler.data.repository.user;

import guru.qa.niffler.data.entity.AuthorityEntity;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.data.sjdbc.UserAuthEntityRowMapper;
import guru.qa.niffler.data.sjdbc.UserEntityRowMapper;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.data.Database.AUTH;
import static guru.qa.niffler.data.Database.USERDATA;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class UserRepositoryJdbc implements UserRepository {

    private static final DataSource authDataSource = DataSourceProvider.dataSource(AUTH);
    private static final DataSource userDataSource = DataSourceProvider.dataSource(USERDATA);
    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public UserAuthEntity createUserInAuth(UserAuthEntity user) {
        try (Connection connection = authDataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement userPreparedStatement = connection.prepareStatement(
                    """
                            INSERT INTO "user" (
                            username,
                            password,
                            enabled,
                            account_non_expired,
                            account_non_locked,
                            credentials_non_expired)
                            VALUES (?, ?, ?, ?, ?, ?)""",
                    RETURN_GENERATED_KEYS
            );
                 PreparedStatement authorityPreparedStatement = connection.prepareStatement(
                         "INSERT INTO authority (user_id, authority) VALUES (?, ?)")
            ) {
                userPreparedStatement.setString(1, user.getUsername());
                userPreparedStatement.setString(2, passwordEncoder.encode(user.getPassword()));
                userPreparedStatement.setBoolean(3, user.getEnabled());
                userPreparedStatement.setBoolean(4, user.getAccountNonExpired());
                userPreparedStatement.setBoolean(5, user.getAccountNonLocked());
                userPreparedStatement.setBoolean(6, user.getCredentialsNonExpired());

                userPreparedStatement.executeUpdate();

                UUID generatedUserId;

                try (ResultSet resultSet = userPreparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedUserId = UUID.fromString(resultSet.getString("id"));
                    } else {
                        throw new IllegalStateException("Can't access to id");
                    }
                }

                user.setId(generatedUserId);

                for (AuthorityEntity authority : user.getAuthorities()) {
                    authorityPreparedStatement.setObject(1, generatedUserId);
                    authorityPreparedStatement.setString(2, authority.getAuthority().name());
                    authorityPreparedStatement.addBatch();
                    authorityPreparedStatement.clearParameters();
                }

                authorityPreparedStatement.executeBatch();
                connection.commit();

                return user;

            } catch (SQLException e) {
                connection.rollback();
                throw e;

            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity createUserInUserData(UserEntity user) {
        try (Connection connection = userDataSource.getConnection();
             PreparedStatement userPreparedStatement = connection.prepareStatement(
                     """
                             INSERT INTO "user" (
                             username,
                             currency,
                             firstname,
                             surname,
                             photo,
                             photo_small)
                             VALUES (?, ?, ?, ?, ?, ?)""",
                     RETURN_GENERATED_KEYS
             )) {
            userPreparedStatement.setString(1, user.getUsername());
            userPreparedStatement.setString(2, user.getCurrency().name());
            userPreparedStatement.setString(3, user.getFirstname());
            userPreparedStatement.setString(4, user.getSurname());
            userPreparedStatement.setObject(5, user.getPhoto());
            userPreparedStatement.setObject(6, user.getPhotoSmall());

            userPreparedStatement.executeUpdate();

            UUID generatedUserId;

            try (ResultSet resultSet = userPreparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedUserId = UUID.fromString(resultSet.getString("id"));
                } else {
                    throw new IllegalStateException("Can't access to id");
                }
            }

            user.setId(generatedUserId);

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserAuthEntity findUserAuthByUsername(String username) {
        try (Connection connection = userDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     (
                             "SELECT * FROM \"user\" WHERE username = ?"
                     )) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            UserAuthEntity userAuthEntity = null;

            while (resultSet.next()) {
                userAuthEntity = UserAuthEntityRowMapper.INSTANCE.mapRow(resultSet, 0);
            }

            return userAuthEntity;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity findUserInUserdataById(UUID id) {
        try (Connection connection = userDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM \"user\" WHERE id = ?")
        ) {

            preparedStatement.setObject(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            UserEntity userEntity = null;

            while (resultSet.next()) {
                userEntity = UserEntityRowMapper.INSTANCE.mapRow(resultSet, 0);
            }

            return userEntity;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserEntity> findUserByUsername(String username) {
        try (Connection connection = userDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     (
                             "SELECT * FROM \"user\" WHERE username = ?"
                     )) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            List<UserEntity> userEntities = new ArrayList<>();

            while (resultSet.next()) {
                UserEntity user = UserEntityRowMapper.INSTANCE.mapRow(resultSet, 0);
                userEntities.add(user);
            }

            return userEntities;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserAuthEntity updateUserInAuth(UserAuthEntity user) {
        try (Connection connection = authDataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement userPreparedStatement = connection.prepareStatement(
                    """
                            UPDATE "user"
                            SET username = ?,
                            password = ?,
                            enabled = ?,
                            account_non_expired = ?,
                            account_non_locked = ?,
                            credentials_non_expired= ?
                            WHERE id = ?""");

                 PreparedStatement deleteAuthorityPreparedStatement = connection.prepareStatement(
                         "DELETE FROM authority WHERE user_id = ?");

                 PreparedStatement authUpdatePreparedStatement = connection.prepareStatement(
                         "INSERT INTO authority (user_id, authority) VALUES (?, ?)")
            ) {
                userPreparedStatement.setString(1, user.getUsername());
                userPreparedStatement.setString(2, passwordEncoder.encode(user.getPassword()));
                userPreparedStatement.setBoolean(3, user.getEnabled());
                userPreparedStatement.setBoolean(4, user.getAccountNonExpired());
                userPreparedStatement.setBoolean(5, user.getAccountNonLocked());
                userPreparedStatement.setBoolean(6, user.getCredentialsNonExpired());
                userPreparedStatement.setObject(7, user.getId());

                userPreparedStatement.executeUpdate();

                deleteAuthorityPreparedStatement.setObject(1, user.getId());

                deleteAuthorityPreparedStatement.executeUpdate();


                for (AuthorityEntity authority : user.getAuthorities()) {
                    authUpdatePreparedStatement.setObject(1, user.getId());
                    authUpdatePreparedStatement.setString(2, authority.getAuthority().name());
                    authUpdatePreparedStatement.addBatch();
                    authUpdatePreparedStatement.clearParameters();
                }

                authUpdatePreparedStatement.executeBatch();
                connection.commit();

                return user;

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity updateUserInUserdata(UserEntity user) {
        try (Connection connection = userDataSource.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(
                     """
                             UPDATE "user"
                             SET username = ?,
                             "currency = ?,
                             "firstname = ?,
                             "surname = ?,
                             "photo = ?,
                             "photo_small = ?
                             "WHERE id = ?""")) {

            updateStatement.setString(1, user.getUsername());
            updateStatement.setString(2, user.getCurrency().name());
            updateStatement.setString(3, user.getFirstname());
            updateStatement.setObject(4, user.getSurname());
            updateStatement.setObject(5, user.getPhoto());
            updateStatement.setObject(6, user.getPhotoSmall());
            updateStatement.setObject(7, user.getId());
            updateStatement.executeUpdate();

            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}