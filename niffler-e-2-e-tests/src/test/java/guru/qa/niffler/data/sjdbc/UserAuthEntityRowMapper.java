package guru.qa.niffler.data.sjdbc;

import guru.qa.niffler.data.entity.UserAuthEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserAuthEntityRowMapper implements RowMapper<UserAuthEntity> {

    public static final UserAuthEntityRowMapper instance = new UserAuthEntityRowMapper();

    private UserAuthEntityRowMapper() {

    }

    @Override
    public UserAuthEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserAuthEntity userEntity = new UserAuthEntity();
        userEntity.setUsername(rs.getString("username"));
        userEntity.setPassword(rs.getString("password"));
        userEntity.setEnabled(rs.getBoolean("enabled"));
        userEntity.setAccountNonExpired(rs.getBoolean("account_non_expired"));
        userEntity.setAccountNonLocked(rs.getBoolean("account_non_locked"));
        userEntity.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
        userEntity.setId((UUID) rs.getObject("id"));
        return userEntity;
    }
}
