package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.AuthorityEntity;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.model.CurrencyValues;
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

    private static final DataSource authDataSource = DataSourceProvider.dataSource(DataBase.AUTH);
    private static final DataSource udDataSource = DataSourceProvider.dataSource(DataBase.USERDATA);
    private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public UserAuthEntity createUserInAuth(UserAuthEntity user) {
        try (Connection conn = authDataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement userPs = conn.prepareStatement(
                    "INSERT INTO \"user\" (" +
                            "username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)" +
                            " VALUES (?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
                 PreparedStatement authorityPs = conn.prepareStatement(
                         "INSERT INTO \"authority\" (" +
                                 "user_id, authority)" +
                                 " VALUES (?, ?)"
                 )) {
                userPs.setString(1, user.getUsername());
                userPs.setString(2, pe.encode(user.getPassword()));
                userPs.setBoolean(3, user.getEnabled());
                userPs.setBoolean(4, user.getAccountNonExpired());
                userPs.setBoolean(5, user.getAccountNonLocked());
                userPs.setBoolean(6, user.getCredentialsNonExpired());
                userPs.executeUpdate();

                UUID generatedUserId;
                try (ResultSet resultSet = userPs.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedUserId = UUID.fromString(resultSet.getString("id"));
                    } else {
                        throw new IllegalStateException("Can`t access to id");
                    }
                }
                user.setId(generatedUserId);

                for (AuthorityEntity authorityEntity : user.getAuthorities()) {
                    authorityPs.setObject(1, generatedUserId);
                    authorityPs.setString(2, authorityEntity.getAuthority().name());
                    authorityPs.addBatch();
                    authorityPs.clearParameters();
                }

                authorityPs.executeBatch();
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

    @Override
    public UserEntity createUserInUserdata(UserEntity user) {
        try (Connection conn = udDataSource.getConnection();
             PreparedStatement userPs = conn.prepareStatement(
                     "INSERT INTO \"user\" (" +
                             "username, currency, firstname, surname, photo, photo_small)" +
                             " VALUES (?, ?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            userPs.setString(1, user.getUsername());
            userPs.setString(2, user.getCurrency().name());
            userPs.setString(3, user.getFirstname());
            userPs.setString(4, user.getSurname());
            userPs.setObject(5, user.getPhoto());
            userPs.setObject(6, user.getPhotoSmall());
            userPs.executeUpdate();

            UUID generatedUserId;
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


    @Override
    public UserAuthEntity updateUserInAuth(UserAuthEntity user) {
        try (Connection connection = authDataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement updateUserStmt = connection.prepareStatement(
                    "UPDATE \"user\" SET username = ?, password = ?, enabled = ?, account_non_expired = ?," +
                            " account_non_locked = ?, credentials_non_expired = ? WHERE id = ?");
                 PreparedStatement deleteAuthorityStmt = connection.prepareStatement(
                         "DELETE FROM\"authority\" WHERE user_id = ?");
                 PreparedStatement insertAuthorityStmt = connection.prepareStatement(
                         "INSERT INTO \"authority\" (" +
                                 "user_id, authority)" +
                                 " VALUES (?, ?)"
                 )) {

                updateUserStmt.setString(1, user.getUsername());
                updateUserStmt.setString(2, pe.encode(user.getPassword()));
                updateUserStmt.setBoolean(3, user.getEnabled());
                updateUserStmt.setBoolean(4, user.getAccountNonExpired());
                updateUserStmt.setBoolean(5, user.getAccountNonLocked());
                updateUserStmt.setBoolean(6, user.getCredentialsNonExpired());
                updateUserStmt.setObject(7, user.getId());
                updateUserStmt.executeUpdate();

                deleteAuthorityStmt.setObject(1, user.getId());
                deleteAuthorityStmt.executeUpdate();

                for (AuthorityEntity authorityEntity : user.getAuthorities()) {
                    insertAuthorityStmt.setObject(1, user.getId());
                    insertAuthorityStmt.setString(2, authorityEntity.getAuthority().name());
                    insertAuthorityStmt.addBatch();
                    insertAuthorityStmt.clearParameters();
                }

                insertAuthorityStmt.executeBatch();
                connection.commit();
                connection.setAutoCommit(true);

                return user;
            } catch (SQLException e) {
                throw new RuntimeException("Error updating user in userdata and authority using batch update", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public UserEntity updateUserInUserdata(UserEntity user) {
        try (Connection conn = udDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE \"user\" SET username = ?, currency = ?, " +
                     "firstname = ?, surname = ?, photo = ?, photo_small = ? WHERE id = ?")) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setObject(5, user.getPhoto());
            ps.setObject(6, user.getPhotoSmall());
            ps.setObject(7, user.getId());
            ps.executeUpdate();

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating user in userdata", e);
        }

        return null;
    }

    @Override
    public Optional<UserEntity> findUserInUserdataById(UUID id) {
        UserEntity user = new UserEntity();
        try (Connection conn = udDataSource.getConnection();
             PreparedStatement userPs = conn.prepareStatement(
                     "SELECT * FROM \"user\" WHERE id = ?")) {
            userPs.setObject(1, id);
            userPs.execute();

            try (ResultSet resultSet = userPs.getResultSet()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getObject("id", UUID.class));
                    user.setUsername(resultSet.getString("username"));
                    user.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                    user.setFirstname(resultSet.getString("firstname"));
                    user.setSurname(resultSet.getString("surname"));
                    user.setPhoto(resultSet.getBytes("photo"));
                    user.setPhotoSmall(resultSet.getBytes("photo_small"));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(user);
    }

    @Override
    public Optional<UserEntity> findUserInUserdataByUsername(String username) {
        UserEntity user = new UserEntity();
        try (Connection conn = udDataSource.getConnection();
             PreparedStatement userPs = conn.prepareStatement(
                     "SELECT * FROM \"user\" WHERE username = ?")) {
            userPs.setObject(1, username);
            userPs.execute();

            try (ResultSet resultSet = userPs.getResultSet()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getObject("id", UUID.class));
                    user.setUsername(resultSet.getString("username"));
                    user.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                    user.setFirstname(resultSet.getString("firstname"));
                    user.setSurname(resultSet.getString("surname"));
                    user.setPhoto(resultSet.getBytes("photo"));
                    user.setPhotoSmall(resultSet.getBytes("photo_small"));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(user);
    }


    @Override
    public Optional<UserAuthEntity> findUserInAuthByUsername(String username) {
        UserAuthEntity user = new UserAuthEntity();
        try (Connection connection = authDataSource.getConnection();
             PreparedStatement userPs = connection.prepareStatement(
                     "SELECT * FROM \"user\" WHERE username = ?")) {
            userPs.setObject(1, username);
            userPs.execute();

            try (ResultSet resultSet = userPs.getResultSet()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getObject("id", UUID.class));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    user.setEnabled(resultSet.getBoolean("enabled"));
                    user.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
                    user.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
                    user.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));

                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(user);
    }

}