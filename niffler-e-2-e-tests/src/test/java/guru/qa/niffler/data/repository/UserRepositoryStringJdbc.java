package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.data.sjdbc.UserAuthEntityRowMapper;
import guru.qa.niffler.data.sjdbc.UserEntityRowMapper;
import guru.qa.niffler.enums.Authority;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class UserRepositoryStringJdbc implements UserRepository {

	private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	public static JdbcTemplate jdbcAuthTemplate = new JdbcTemplate(DataSourceProvider.dataSource(DataBase.AUTH));
	public static JdbcTemplate jdbcUDTemplate = new JdbcTemplate(DataSourceProvider.dataSource(DataBase.USERDATA));
	public static TransactionTemplate authTxTemplate =
			new TransactionTemplate(new JdbcTransactionManager(DataSourceProvider.dataSource(DataBase.AUTH)));

	@Override
	public UserAuthEntity createUserInAuth(UserAuthEntity userAuthEntity) {
		return authTxTemplate.execute(status -> {
			KeyHolder kh = new GeneratedKeyHolder();
			jdbcAuthTemplate.update(con -> {
						PreparedStatement ps = con.prepareStatement(
								"INSERT INTO \"user\" (" +
										"username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)" +
										" VALUES (?, ?, ?, ?, ?, ?)",
								PreparedStatement.RETURN_GENERATED_KEYS
						);
						ps.setString(1, userAuthEntity.getUsername());
						ps.setString(2, pe.encode(userAuthEntity.getPassword()));
						ps.setBoolean(3, userAuthEntity.getEnabled());
						ps.setBoolean(4, userAuthEntity.getAccountNonExpired());
						ps.setBoolean(5, userAuthEntity.getAccountNonLocked());
						ps.setBoolean(6, userAuthEntity.getCredentialsNonExpired());
						return ps;
					}, kh
			);
			userAuthEntity.setId((UUID) kh.getKeys().get("id"));

			jdbcAuthTemplate.batchUpdate(
					"INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)",
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							ps.setObject(1, userAuthEntity.getId());
							ps.setString(2, Authority.values()[i].name());
						}

						@Override
						public int getBatchSize() {
							return userAuthEntity.getAuthorities().size();
						}
					}
			);
			return userAuthEntity;
		});
	}

	@Override
	public UserEntity createUserInUserdata(UserEntity userEntity) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcUDTemplate.update(con -> {
					PreparedStatement ps = con.prepareStatement(
							"INSERT INTO \"user\" (" +
									"username, currency,firstname,surname,photo," +
									"photo_small) VALUES(?,?,?,?,?,?)",
							PreparedStatement.RETURN_GENERATED_KEYS
					);
					ps.setString(1, userEntity.getUsername());
					ps.setString(2, userEntity.getCurrency().name());
					ps.setString(3, userEntity.getFirstname());
					ps.setString(4, userEntity.getSurname());
					ps.setObject(5, userEntity.getPhoto());
					ps.setObject(6, userEntity.getPhotoSmall());
					return ps;
				}, keyHolder
		);
		userEntity.setId((UUID) keyHolder.getKeys().get("id"));
		return userEntity;
	}

	@Override
	public UserAuthEntity updateUserInAuth(UserAuthEntity userAuthEntity, List<Authority> listAuthority) {
		jdbcAuthTemplate.update(
				"DELETE  FROM \"authority\"  WHERE user_id=(select id from \"user\" where username = ?)",
				userAuthEntity.getUsername()
		);
		return authTxTemplate.execute(status -> {
			jdbcAuthTemplate.update(
					"UPDATE  \"user\" SET password=?,enabled=?,account_non_expired=?,account_non_locked=?," +
							"credentials_non_expired=? WHERE username=?",
					pe.encode(userAuthEntity.getPassword()),
					userAuthEntity.getEnabled(),
					userAuthEntity.getAccountNonExpired(),
					userAuthEntity.getAccountNonLocked(),
					userAuthEntity.getCredentialsNonExpired(),
					userAuthEntity.getUsername()
			);

			jdbcAuthTemplate.batchUpdate(
					"INSERT INTO \"authority\" (user_id,authority) VALUES ((select id from \"user\" where username =?),?)",
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							ps.setObject(1, userAuthEntity.getUsername());
							ps.setString(2, Authority.values()[i].name());
						}

						@Override
						public int getBatchSize() {
							return listAuthority.size();
						}
					}
			);
			return userAuthEntity;
		});
	}

	@Override
	public UserEntity updateUserInUserdata(UserEntity userEntity) {
		jdbcUDTemplate.update(
				"UPDATE \"user\" SET " +
						"currency=?,firstname=?,surname=? where username=?",
				userEntity.getCurrency().name(),
				userEntity.getFirstname(),
				userEntity.getSurname(),
				userEntity.getUsername());
		return userEntity;
	}

	@Override
	public Optional<UserEntity> findUserInUserDataByID(UUID id) {
		try {
			return Optional.ofNullable(jdbcUDTemplate.queryForObject(
					"SELECT * FROM \"user\" WHERE id = ?",
					UserEntityRowMapper.instance,
					id
			));
		} catch (DataRetrievalFailureException e) {
			return Optional.empty();
		}
	}

	@Override
	public Optional<UserAuthEntity> findUserInAuthByUsername(String username) {
		try {
			return Optional.ofNullable(jdbcAuthTemplate.queryForObject(
					"SELECT * FROM \"user\" WHERE username = ?",
					UserAuthEntityRowMapper.instance,
					username
			));
		} catch (DataRetrievalFailureException e) {
			return Optional.empty();
		}
	}

	@Override
	public Optional<UserEntity> findInUserdataByUsername(String username) {
		try {
			return Optional.ofNullable(jdbcUDTemplate.queryForObject(
					"SELECT * FROM \"user\" WHERE username = ?",
					UserEntityRowMapper.instance,
					username
			));
		} catch (DataRetrievalFailureException e) {
			return Optional.empty();
		}
	}

}
