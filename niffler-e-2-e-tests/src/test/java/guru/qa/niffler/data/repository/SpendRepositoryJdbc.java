package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import io.qameta.allure.Step;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SpendRepositoryJdbc implements SpendRepository {

	private static final DataSource spendDataSource =
			DataSourceProvider.dataSource(DataBase.SPEND);

	@Step("Создать категорию")
	public CategoryEntity createCategory(CategoryEntity category) {
		try (Connection connection =
					 spendDataSource.getConnection();
			 PreparedStatement ps =
					 connection.prepareStatement(
							 "INSERT INTO category (category,username) VALUES(?,?);",
							 PreparedStatement.RETURN_GENERATED_KEYS
					 )) {
			ps.setString(1, category.getCategory());
			ps.setString(2, category.getUsername());
			ps.executeUpdate();
			UUID generateId = null;
			try (ResultSet result = ps.getGeneratedKeys()) {
				if (result.next()) {
					generateId = UUID.fromString(result.getString("id"));
				} else {
					throw new IllegalArgumentException("Не удалось получить uuid категории");
				}
			}
			category.setId(generateId);
			return category;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Step("Изменить категорию")
	public CategoryEntity editCategory(CategoryEntity category) {
		try (Connection connection =
					 spendDataSource.getConnection();
			 PreparedStatement ps =
					 connection.prepareStatement(
							 "UPDATE category set category = ?, username = ? where id = ?;"
					 )) {
			ps.setString(1, category.getCategory());
			ps.setString(2, category.getUsername());
			ps.setString(3, String.valueOf(category.getId()));
			ps.executeUpdate();
			try (ResultSet result = ps.getResultSet()) {
				if (result.next()) {
					category.setCategory(result.getString("category"));
					category.setUsername(result.getString("username"));
				} else {
					throw new RuntimeException();
				}
			}
			return category;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Step("Удалить категорию")
	public void removeCategory(CategoryEntity category) {
		try (Connection connection =
					 spendDataSource.getConnection();
			 PreparedStatement ps =
					 connection.prepareStatement(
							 "DELETE FROM category WHERE id = ?;"
					 )) {
			ps.setObject(1, category.getId());
			ps.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Step("Создать трату")
	public SpendEntity createSpend(SpendEntity spend) {
		try (Connection connection =
					 spendDataSource.getConnection();
			 PreparedStatement ps =
					 connection.prepareStatement(
							 "INSERT INTO spend (username,spend_date,currency,amount,description,category_id)" +
									 " VALUES(?,?,?,?,?,?);",
							 PreparedStatement.RETURN_GENERATED_KEYS
					 )) {
			ps.setString(1, spend.getUsername());
			ps.setString(2, String.valueOf(spend.getSpendDate()));
			ps.setString(3, String.valueOf(spend.getCurrency()));
			ps.setString(4, String.valueOf(spend.getAmount()));
			ps.setString(5, spend.getDescription());
			ps.setString(6, spend.getCategory());
			ps.executeUpdate();
			UUID generateId = null;
			try (ResultSet result = ps.getResultSet();) {
				if (result.next()) {
					generateId = UUID.fromString(result.getString("id"));
				} else {
					throw new RuntimeException();
				}
			}
			spend.setId(generateId);
			return spend;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Step("Изменить трату")
	public SpendEntity editSpend(SpendEntity spend) {
		try (Connection connection =
					 spendDataSource.getConnection();
			 PreparedStatement ps =
					 connection.prepareStatement(
							 "UPDATE spend set username =?,currency=?,amount=?,description=?," +
									 "category_id=? where id = ?;"
							 )) {
			ps.setString(1, spend.getUsername());
			ps.setString(2, String.valueOf(spend.getCurrency()));
			ps.setString(3, String.valueOf(spend.getAmount()));
			ps.setString(4, spend.getDescription());
			ps.setString(5, spend.getCategory());
			ps.setString(6, String.valueOf(spend.getId()));
			ps.executeUpdate();
			try (ResultSet result = ps.getResultSet();) {
				if (result.next()) {
					spend.setUsername(result.getString("username"));
					spend.setCategory(result.getString("currency"));
					spend.setAmount(Double.valueOf(result.getString("amount")));
					spend.setDescription(result.getString("description"));
					spend.setCategory(result.getString("category_id"));
				} else {
					throw new RuntimeException();
				}
			}
			return spend;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Step("Удалить трату")
	public void removeSpend(SpendEntity spend) {
		try (Connection connection =
					 spendDataSource.getConnection();
			 PreparedStatement ps =
					 connection.prepareStatement(
							 "DELETE FROM spend WHERE id = ?;"
					 )) {
			ps.setObject(1, spend.getId());
			ps.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
