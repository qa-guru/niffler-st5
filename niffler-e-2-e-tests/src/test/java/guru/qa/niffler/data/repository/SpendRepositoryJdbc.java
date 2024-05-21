package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.model.CurrencyValues;
import io.qameta.allure.Step;

import javax.sql.DataSource;
import java.sql.*;
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
			ps.setObject(3, category.getId());
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
			ps.setDate(2, new Date(spend.getSpendDate().getTime()));
			ps.setString(3, spend.getCurrency().name());
			ps.setDouble(4, spend.getAmount());
			ps.setString(5, spend.getDescription());
			ps.setObject(6, spend.getCategoryId());
			ps.executeUpdate();
			UUID generateId = null;
			try (ResultSet result = ps.getGeneratedKeys()) {
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
							 "UPDATE spend set username =?,currency=?, spend_data = ?,amount=?,description=?," +
									 "category_id=? where id = ?;"
					 )) {
			ps.setString(1, spend.getUsername());
			ps.setString(2, spend.getCurrency().name());
			ps.setDate(3, new Date(spend.getSpendDate().getTime()));
			ps.setDouble(4, spend.getAmount());
			ps.setString(5, spend.getDescription());
			ps.setObject(6, spend.getCategoryId());
			ps.setObject(7, spend.getId());
			ps.executeUpdate();
			try (ResultSet result = ps.getResultSet();) {
				if (result.next()) {
					spend.setUsername(result.getString("username"));
					spend.setCurrency(CurrencyValues.valueOf(result.getString("currency")));
					spend.setSpendDate(result.getDate("spend_data"));
					spend.setAmount(Double.valueOf(result.getString("amount")));
					spend.setDescription(result.getString("description"));
					spend.setCategoryId(UUID.fromString(result.getString("category_id")));
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
