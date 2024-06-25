package guru.qa.niffler.data.repository.user;

import guru.qa.niffler.data.Database;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.data.sjdbc.UserAuthEntityRowMapper;
import guru.qa.niffler.data.sjdbc.UserEntityRowMapper;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class UserRepositorySpringJdbc implements UserRepository {

    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private static final TransactionTemplate authTxTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSourceProvider.dataSource(Database.AUTH)
            )
    );

    private static final JdbcTemplate authJdbcTemplate = new JdbcTemplate(
            DataSourceProvider.dataSource(Database.AUTH)
    );

    public static final JdbcTemplate userDataJdbcTemplate = new JdbcTemplate(
            DataSourceProvider.dataSource(Database.USERDATA)
    );

    @Override
    public UserAuthEntity createUserInAuth(UserAuthEntity user) {
        return authTxTemplate.execute(status -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            authJdbcTemplate.update(con -> {
                        PreparedStatement ps = con.prepareStatement(
                                """
                                        INSERT INTO "user" (
                                        username,
                                        password,
                                        enabled,
                                        account_non_expired,
                                        account_non_locked,
                                        credentials_non_expired)
                                        VALUES (?, ?, ?, ?, ?, ?)
                                        """,
                                RETURN_GENERATED_KEYS
                        );
                        ps.setString(1, user.getUsername());
                        ps.setString(2, passwordEncoder.encode(user.getPassword()));
                        ps.setBoolean(3, user.getEnabled());
                        ps.setBoolean(4, user.getAccountNonExpired());
                        ps.setBoolean(5, user.getAccountNonLocked());
                        ps.setBoolean(6, user.getCredentialsNonExpired());
                        return ps;
                    }, keyHolder
            );

            user.setId((UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id"));

            authJdbcTemplate.batchUpdate(
                    "INSERT INTO authority (user_id, authority) VALUES (?, ?)",
                    new BatchPreparedStatementSetter() {

                        @Override
                        public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                            ps.setObject(1, user.getId());
                            ps.setString(2, user.getAuthorities().get(i).getAuthority().name());
                        }

                        @Override
                        public int getBatchSize() {
                            return user.getAuthorities().size();
                        }
                    }
            );
            return user;
        });
    }

    @Override
    public UserEntity createUserInUserData(UserEntity user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        userDataJdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(
                            """
                                    INSERT INTO "user" (
                                    username,
                                    currency,
                                    firstname,
                                    surname,
                                    photo,
                                    photo_small)
                                    VALUES (?, ?, ?, ?, ?, ?)
                                    """,
                            RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, user.getUsername());
                    preparedStatement.setString(2, user.getCurrency().name());
                    preparedStatement.setString(3, user.getFirstname());
                    preparedStatement.setString(4, user.getSurname());
                    preparedStatement.setObject(5, user.getPhoto());
                    preparedStatement.setObject(6, user.getPhotoSmall());
                    return preparedStatement;
                }, keyHolder
        );

        user.setId((UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id"));
        return user;
    }

    @Override
    public UserAuthEntity findUserAuthByUsername(String username) {
        return authJdbcTemplate.queryForObject(
                "SELECT * FROM \"user\" WHERE username = ?",
                UserAuthEntityRowMapper.INSTANCE,
                username
        );
    }

    @Override
    public UserEntity findUserInUserdataById(UUID id) {
        return Optional.ofNullable(userDataJdbcTemplate.queryForObject(
                "SELECT * FROM \"user\" WHERE id = ?",
                UserEntityRowMapper.INSTANCE,
                id)).orElseThrow();
    }

    @Override
    public List<UserEntity> findUserByUsername(String username) {
        List<Map<String, Object>> rows = userDataJdbcTemplate.queryForList(
                "SELECT * FROM \"user\" WHERE username = ?", username
        );

        return rows.stream().map(
                        row -> {
                            UserEntity user = new UserEntity();
                            user.setUsername(username);
                            user.setCurrency(CurrencyValues.valueOf((String) row.get("currency")));
                            user.setFirstname((String) row.get("firstname"));
                            user.setSurname((String) row.get("surname"));
                            user.setPhoto((byte[]) row.get("photo"));
                            user.setPhotoSmall((byte[]) row.get("photo_small"));
                            user.setId((UUID) row.get("id"));
                            return user;
                        }
                )
                .toList();
    }

    @Override
    public UserAuthEntity updateUserInAuth(UserAuthEntity user) {
        return authTxTemplate.execute(status -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            authJdbcTemplate.update(con -> {
                        PreparedStatement ps = con.prepareStatement(
                                """
                                        UPDATE "user"
                                        SET username = ?,
                                        password= ?,
                                        enabled= ?,
                                        account_non_expired= ?,
                                        account_non_locked= ?,
                                        credentials_non_expired= ?
                                        WHERE id = ?""",
                                RETURN_GENERATED_KEYS
                        );

                        ps.setString(1, user.getUsername());
                        ps.setString(2, passwordEncoder.encode(user.getPassword()));
                        ps.setBoolean(3, user.getEnabled());
                        ps.setBoolean(4, user.getAccountNonExpired());
                        ps.setBoolean(5, user.getAccountNonLocked());
                        ps.setBoolean(6, user.getCredentialsNonExpired());
                        ps.setObject(7, user.getId());
                        return ps;
                    }, keyHolder
            );

            user.setId((UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id"));

            authJdbcTemplate.update("DELETE FROM authority WHERE user_id = ?", user.getId());

            authJdbcTemplate.batchUpdate(
                    "INSERT INTO authority (user_id, authority) VALUES (?, ?)",
                    new BatchPreparedStatementSetter() {

                        @Override
                        public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                            ps.setObject(1, user.getId());
                            ps.setString(2, user.getAuthorities().get(i).getAuthority().name());
                        }

                        @Override
                        public int getBatchSize() {
                            return user.getAuthorities().size();
                        }
                    }
            );
            return user;
        });


    }

    @Override
    public UserEntity updateUserInUserdata(UserEntity user) {
        userDataJdbcTemplate.update(
                """
                         UPDATE "user"
                         SET username = ?,
                         currency = ?,
                         firstname = ?,
                         surname = ?,
                         photo = ?,
                         photo_small = ?
                        WHERE id = ?""",
                user.getUsername(),
                user.getCurrency().name(),
                user.getFirstname(),
                user.getSurname(),
                user.getPhoto(),
                user.getPhotoSmall(),
                user.getId());

        return user;
    }

}
