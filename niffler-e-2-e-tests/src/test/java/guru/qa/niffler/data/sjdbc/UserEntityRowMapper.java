package guru.qa.niffler.data.sjdbc;

// Класс UserEntityRowMapper для маппинга результатов запроса к объекту UserEntity

import guru.qa.niffler.data.entity.CurrencyValues;
import guru.qa.niffler.data.entity.UserEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserEntityRowMapper implements RowMapper<UserEntity> {

    // Static field для хранения единственного экземпляра класса
    public static final UserEntityRowMapper instance = new UserEntityRowMapper();

    // Private constructor для предотвращения создания экземпляров класса
    private UserEntityRowMapper() {
    }

    // Метод mapRow для маппинга результатов запроса к объекту UserEntity
    @Override
    public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Создаем новый объект UserEntity для возвращаемого пользователя
        UserEntity userEntity = new UserEntity();
        // Устанавливаем значения для полей объекта
        userEntity.setId((UUID) rs.getObject("id"));
        userEntity.setUsername(rs.getString("username"));
        // Устанавливаем валюту из строки в Enum-значение
        userEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        // Возвращаем созданный объект
        return userEntity;
    }
}