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
        UserAuthEntity userAuthEntity = new UserAuthEntity();
        userAuthEntity.setId((UUID) rs.getObject("id"));
        userAuthEntity.setUsername(rs.getString("username"));
        userAuthEntity.setPassword(rs.getString("password"));
        userAuthEntity.setEnabled(rs.getBoolean("enabled"));
        userAuthEntity.setAccountNonExpired(rs.getBoolean("account_non_expired"));
        userAuthEntity.setAccountNonLocked(rs.getBoolean("account_non_locked"));
        userAuthEntity.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
        return userAuthEntity;
    }
}