package guru.qa.niffler.data.sjdbc;

import guru.qa.niffler.data.entity.UserAuthEntity;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UserAuthEntityRowMapper implements RowMapper<UserAuthEntity> {

    public static final UserAuthEntityRowMapper INSTANCE = new UserAuthEntityRowMapper();

    @Override
    public UserAuthEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserAuthEntity userAuthEntity = new UserAuthEntity();
        userAuthEntity.setUsername(rs.getString("username"));
        userAuthEntity.setEnabled(rs.getBoolean("enabled"));
        userAuthEntity.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
        userAuthEntity.setAccountNonLocked(rs.getBoolean("account_non_locked"));
        userAuthEntity.setAccountNonExpired(rs.getBoolean("account_non_expired"));
        userAuthEntity.setId(rs.getObject("id", UUID.class));
        return userAuthEntity;
    }

}