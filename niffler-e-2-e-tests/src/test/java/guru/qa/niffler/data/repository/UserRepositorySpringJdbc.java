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

// Класс UserRepositorySpringJdbc реализует интерфейс UserRepository для работы с пользователями в базе данных
// с использованием Spring JDBC и Spring Security
public class UserRepositorySpringJdbc implements UserRepository {

    // Static field для хранения пароля
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    // Static fields для хранения шаблонов транзакций и jdbcTemplate для аутентификации
    private final static TransactionTemplate authTxTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSourceProvider.dataSource(DataBase.AUTH)));

    // Static field для хранения jdbcTemplate для аутентификации
    private final static JdbcTemplate authJdbcTemplate = new JdbcTemplate(
            DataSourceProvider.dataSource(DataBase.AUTH));

    // Static field для хранения jdbcTemplate для данных пользователя
    private final static JdbcTemplate userDataJdbcTemplate = new JdbcTemplate(
            DataSourceProvider.dataSource(DataBase.USERDATA));

    // Метод createUserInAuth создает нового пользователя в таблице аутентификации
    @Override
    public UserAuthEntity createUserInAuth(UserAuthEntity user) {
        // Используя TransactionTemplate, выполняется транзакция
        return authTxTemplate.execute(status -> {
            // Используя GeneratedKeyHolder, получаем идентификатор созданного пользователя
            KeyHolder kh = new GeneratedKeyHolder();
            // Используя jdbcTemplate, выполняется запрос на создание пользователя
            authJdbcTemplate.update(con -> {
                        // Создаем подготовленное заявление для запроса
                        PreparedStatement ps = con.prepareStatement(
                                """
                                        INSERT INTO "user" (
                                            username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)
                                        VALUES (?, ?, ?, ?, ?, ?)
                                        """,
                                PreparedStatement.RETURN_GENERATED_KEYS
                        );
                        // Устанавливаем значения для полей запроса
                        ps.setString(1, user.getUsername());
                        ps.setString(2, pe.encode(user.getPassword()));
                        ps.setBoolean(3, user.getEnabled());
                        ps.setBoolean(4, user.getAccountNonExpired());
                        ps.setBoolean(5, user.getAccountNonLocked());
                        ps.setBoolean(6, user.getCredentialsNonExpired());
                        // Возвращаем подготовленное заявление
                        return ps;
                    }, kh
            );
            // Получаем идентификатор созданного пользователя
            user.setId((UUID) kh.getKeys().get("id"));

            // Используя jdbcTemplate, выполняется запрос на создание ролей для пользователя
            authJdbcTemplate.batchUpdate(
                    """
                            INSERT INTO "authority" (
                                user_id, authority)
                            VALUES (?, ?)
                            """,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            // Устанавливаем значения для полей запроса
                            ps.setObject(1, user.getId());
                            ps.setString(2, Authority.values()[i].name());
                        }

                        @Override
                        public int getBatchSize() {
                            // Возвращаем количество ролей для создания
                            return Authority.values().length;
                        }
                    }
            );
            // Возвращаем созданного пользователя
            return user;
        });
    }

    // Метод createUserInUserData создает нового пользователя в таблице данных пользователя
    @Override
    public UserEntity createUserInUserData(UserEntity user) {
        // Используя GeneratedKeyHolder, получаем идентификатор созданного пользователя
        KeyHolder kh = new GeneratedKeyHolder();
        // Используя jdbcTemplate, выполняется запрос на создание пользователя
        userDataJdbcTemplate.update(con -> {
                    // Создаем подготовленное заявление для запроса
                    PreparedStatement ps = con.prepareStatement(
                            """
                                    INSERT INTO "user" (
                                        username, currency, firstname, surname, photo, photo_small)
                                    VALUES (?, ?, ?, ?, ?, ?)
                                    """,
                            PreparedStatement.RETURN_GENERATED_KEYS
                    );
                    // Устанавливаем значения для полей запроса
                    ps.setString(1, user.getUsername());
                    ps.setString(2, user.getCurrency().name());
                    ps.setString(3, user.getFirstname());
                    ps.setString(4, user.getSurname());
                    ps.setObject(5, user.getPhoto());
                    ps.setObject(6, user.getPhotoSmall());
                    // Возвращаем подготовленное заявление
                    return ps;
                }, kh
        );
        // Получаем идентификатор созданного пользователя
        user.setId((UUID) kh.getKeys().get("id"));
        // Возвращаем созданного пользователя
        return user;
    }

    // Метод findUserInUserDataById ищет пользователя в таблице данных пользователя по его идентификатору
    @Override
    public Optional<UserEntity> findUserInUserDataById(UUID id) {
        try {
            // Пытаемся выполнить запрос к базе данных для поиска пользователя по идентификатору;
            // позволяет вернуть Optional.empty(), если запрос не вернул результат
            return Optional.ofNullable(userDataJdbcTemplate.queryForObject( // возвращает объект, соответствующий первой строке результата запроса
                    "SELECT * FROM \"user\" WHERE id = ?",
                    UserEntityRowMapper.instance, // Используем UserEntityRowMapper для маппинга результатов запроса в объекты UserEntity
                    id
            ));
        } catch (DataRetrievalFailureException e) {
            // В случае ошибки при извлечении данных возвращаем Optional.empty()
            return Optional.empty();
        }
    }

}
