package guru.qa.niffler.data.sjdbc;

import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.model.CurrencyValues;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UserEntityRowMapper implements RowMapper<UserEntity> {

    public static final UserEntityRowMapper INSTANCE = new UserEntityRowMapper();

    @Override
    public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserEntity userEntity = new UserEntity();
        userEntity.setId((UUID) rs.getObject("id"));
        userEntity.setUsername(rs.getString("username"));
        userEntity.setFirstname(rs.getString("firstname"));
        userEntity.setSurname(rs.getString("surname"));
        userEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        userEntity.setPhoto((byte[]) rs.getObject("photo"));
        userEntity.setPhotoSmall((byte[]) rs.getObject("photo_small"));
        return userEntity;
    }

}