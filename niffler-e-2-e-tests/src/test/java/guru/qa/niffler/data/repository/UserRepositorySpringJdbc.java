package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.data.sjdbc.UserEntityRowMapper;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserRepositorySpringJdbc implements UserRepository {

    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private static final TransactionTemplate authTxTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSourceProvider.dataSource(DataBase.AUTH)
            )
    );

    private static final JdbcTemplate authJdbcTemplate = new JdbcTemplate(
            DataSourceProvider.dataSource(DataBase.AUTH)
    );

    private static final JdbcTemplate userdataJdbcTemplate = new JdbcTemplate(
            DataSourceProvider.dataSource(DataBase.USERDATA)
    );

    @Override
    public UserAuthEntity createUserInAuth(UserAuthEntity user) {
        return authTxTemplate.execute(status -> {
            KeyHolder kh = new GeneratedKeyHolder();
            authJdbcTemplate.update(con -> {
                        PreparedStatement ps = con.prepareStatement(
                                "INSERT INTO \"user\" (" +
                                        "username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)" +
                                        " VALUES (?, ?, ?, ?, ?, ?)",
                                PreparedStatement.RETURN_GENERATED_KEYS
                        );
                        ps.setString(1, user.getUsername());
                        ps.setString(2, pe.encode(user.getPassword()));
                        ps.setBoolean(3, user.getEnabled());
                        ps.setBoolean(4, user.getAccountNonExpired());
                        ps.setBoolean(5, user.getAccountNonLocked());
                        ps.setBoolean(6, user.getCredentialsNonExpired());
                        return ps;
                    }, kh
            );
            user.setId((UUID) kh.getKeys().get("id"));

            authJdbcTemplate.batchUpdate(
                    "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setObject(1, user.getId());
                            ps.setString(2, Authority.values()[i].name());
                        }

                        @Override
                        public int getBatchSize() {
                            return Authority.values().length;
                        }
                    }
            );
            return user;
        });
    }

    @Override
    public UserEntity createUserInUserdata(UserEntity user) {
        KeyHolder kh = new GeneratedKeyHolder();
        userdataJdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO \"user\" (" +
                                    "username, currency, firstname, surname, photo, photo_small)" +
                                    " VALUES (?, ?, ?, ?, ?, ?)",
                            PreparedStatement.RETURN_GENERATED_KEYS
                    );
                    ps.setString(1, user.getUsername());
                    ps.setString(2, user.getCurrency().name());
                    ps.setString(3, user.getFirstname());
                    ps.setString(4, user.getSurname());
                    ps.setObject(5, user.getPhoto());
                    ps.setObject(6, user.getPhotoSmall());
                    return ps;
                }, kh
        );
        user.setId((UUID) kh.getKeys().get("id"));
        return user;
    }

    @Override
    public Optional<UserEntity> findUserInUserdataById(UUID id) {
        try {
            return Optional.ofNullable(userdataJdbcTemplate.queryForObject(
                    "SELECT * FROM \"user\" WHERE id = ?",
                    UserEntityRowMapper.instance,
                    id
            ));
        } catch (DataRetrievalFailureException e) {
            return Optional.empty();
        }
    }
}
