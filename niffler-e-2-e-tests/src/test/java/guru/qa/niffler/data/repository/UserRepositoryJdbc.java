package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.enums.Authority;
import guru.qa.niffler.enums.CurrencyValues;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryJdbc implements UserRepository {

	private static final DataSource authDataSource =
			DataSourceProvider.dataSource(DataBase.AUTH);
	private static final DataSource udDataSource =
			DataSourceProvider.dataSource(DataBase.USERDATA);
	private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	@Override
	public UserAuthEntity createUserInAuth(UserAuthEntity userAuthEntity) {
		try (Connection conn = authDataSource.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement userPs = conn.prepareStatement(
					"INSERT INTO \"user\" (" +
							"username, password,enabled,account_non_expired,account_non_locked," +
							"credentials_non_expired) VALUES(?,?,?,?,?,?)",
					PreparedStatement.RETURN_GENERATED_KEYS
			);
				 PreparedStatement authorityPs = conn.prepareStatement(
						 "INSERT INTO \"authority\" (user_id,authority) VALUES (?,?)"
				 )
			) {
				userPs.setString(1, userAuthEntity.getUsername());
				userPs.setString(2, pe.encode(userAuthEntity.getPassword()));
				userPs.setBoolean(3, userAuthEntity.getEnabled());
				userPs.setBoolean(4, userAuthEntity.getAccountNonExpired());
				userPs.setBoolean(5, userAuthEntity.getAccountNonLocked());
				userPs.setBoolean(6, userAuthEntity.getCredentialsNonExpired());
				userPs.executeUpdate();

				UUID generateUserId = null;
				try (ResultSet resultSet = userPs.getGeneratedKeys()) {
					if (resultSet.next()) {
						generateUserId = UUID.fromString(resultSet.getString("id"));
					} else {
						throw new IllegalArgumentException("Can not access id");
					}
					userAuthEntity.setId(generateUserId);
					for (Authority authority : Authority.values()) {
						authorityPs.setObject(1, generateUserId);
						authorityPs.setString(2, authority.name());
						authorityPs.addBatch();
						authorityPs.clearParameters();
					}
					authorityPs.executeBatch();
					conn.commit();
					return userAuthEntity;

				} catch (SQLException e) {
					conn.rollback();
					throw e;
				} finally {
					conn.setAutoCommit(true);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public UserEntity createUserInUserdata(UserEntity userEntity) {
		try (Connection conn = udDataSource.getConnection();
			 PreparedStatement userPs = conn.prepareStatement(
					 "INSERT INTO \"user\" (" +
							 "username, currency,firstname,surname,photo," +
							 "photo_small) VALUES(?,?,?,?,?,?)",
					 PreparedStatement.RETURN_GENERATED_KEYS
			 )) {
			userPs.setString(1, userEntity.getUsername());
			userPs.setString(2, userEntity.getCurrency().name());
			userPs.setString(3, userEntity.getFirstname());
			userPs.setString(4, userEntity.getSurname());
			userPs.setObject(5, userEntity.getPhoto());
			userPs.setObject(6, userEntity.getPhotoSmall());
			userPs.executeUpdate();

			UUID generateUserId = null;
			try (ResultSet resultSet = userPs.getGeneratedKeys()) {
				if (resultSet.next()) {
					generateUserId = UUID.fromString(resultSet.getString("id"));
				} else {
					throw new IllegalArgumentException("Can not access id");
				}
				userEntity.setId(generateUserId);
				return userEntity;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public UserAuthEntity updateUserInAuth(UserAuthEntity userAuthEntity, List<Authority> listAuthority) {
		try (Connection conn = authDataSource.getConnection()) {
			PreparedStatement authorityDeletePs = conn.prepareStatement(
					"DELETE  FROM \"authority\"  WHERE user_id=(select id from \"user\" where username =?) "
			);
			authorityDeletePs.setObject(1, userAuthEntity.getUsername());
			authorityDeletePs.executeUpdate();

			conn.setAutoCommit(false);
			try (PreparedStatement userPs = conn.prepareStatement(
					"UPDATE  \"user\" SET" +
							" password=?,enabled=?,account_non_expired=?,account_non_locked=?," +
							"credentials_non_expired=? WHERE username=?"
			);
				 PreparedStatement authorityPs = conn.prepareStatement(
						 "INSERT INTO \"authority\" (user_id,authority) VALUES ((select id from \"user\" where username =?),?)",
						 PreparedStatement.RETURN_GENERATED_KEYS
				 )
			) {
				userPs.setString(1, pe.encode(userAuthEntity.getPassword()));
				userPs.setBoolean(2, userAuthEntity.getEnabled());
				userPs.setBoolean(3, userAuthEntity.getAccountNonExpired());
				userPs.setBoolean(4, userAuthEntity.getAccountNonLocked());
				userPs.setBoolean(5, userAuthEntity.getCredentialsNonExpired());
				userPs.setObject(6, userAuthEntity.getUsername());
				userPs.executeUpdate();

				for (Authority authority : listAuthority) {
					authorityPs.setString(1, userAuthEntity.getUsername());
					authorityPs.setString(2, authority.toString());
					authorityPs.addBatch();
					authorityPs.clearParameters();
				}
				authorityPs.executeBatch();
				conn.commit();
				return userAuthEntity;
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
	public UserEntity updateUserInUserdata(UserEntity userEntity) {
		try (Connection conn = udDataSource.getConnection();
			 PreparedStatement userPs = conn.prepareStatement(
					 "UPDATE \"user\" SET " +
							 "currency=?,firstname=?,surname=?,photo=?," +
							 "photo_small=? where username=?"
			 )) {
			userPs.setString(1, userEntity.getCurrency().name());
			userPs.setString(2, userEntity.getFirstname());
			userPs.setString(3, userEntity.getSurname());
			userPs.setObject(4, userEntity.getPhoto());
			userPs.setObject(5, userEntity.getPhotoSmall());
			userPs.setString(6, userEntity.getUsername());
			userPs.executeUpdate();

			return userEntity;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Optional<UserEntity> findUserInUserDataByID(UUID id) {
		try (Connection conn = udDataSource.getConnection();
			 PreparedStatement userPs = conn.prepareStatement(
					 "SELECT * FROM \"user\" WHERE id = ?"
			 )) {
			userPs.setObject(1, id);
			UserEntity user = new UserEntity();
			try (ResultSet resultSet = userPs.executeQuery()) {
				while (resultSet.next()) {
					user.setUsername(resultSet.getString("username"));
					user.setFirstname(resultSet.getString("firstname"));
					user.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
					user.setSurname(resultSet.getString("surname"));
					user.setId(UUID.fromString(resultSet.getString("id")));
				}
			}
			return Optional.of(user);
		} catch (SQLException e) {
			return Optional.empty();
		}
	}
}
