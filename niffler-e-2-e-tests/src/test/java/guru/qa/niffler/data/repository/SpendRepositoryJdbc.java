package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.model.CurrencyValues;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpendRepositoryJdbc implements SpendRepository {

    private static final DataSource spendDataSource = DataSourceProvider.dataSource(DataBase.SPEND);

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO category (category, username) VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            ps.setString(1, category.getCategory());
            ps.setString(2, category.getUsername());
            ps.executeUpdate();

            UUID generatedId = null;
            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedId = UUID.fromString(resultSet.getString("id"));
                } else {
                    throw new IllegalStateException("Can`t access to id");
                }
            }
            category.setId(generatedId);
            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoryEntity editCategory(CategoryEntity category) {
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE category SET category = ?, username = ? WHERE id = ?")) {
            ps.setString(1, category.getCategory());
            ps.setString(2, category.getUsername());
            ps.setObject(3, category.getId());
            ps.executeUpdate();
            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM category WHERE id = ?"
             )) {
            ps.setObject(1, category.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO spend (username, currency, spend_date, amount, description, category_id)" +
                             " VALUES (?, ?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, spend.getUsername());
            ps.setString(2, spend.getCurrency().name());
            ps.setDate(3, new Date(spend.getSpendDate().getTime()));
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategoryId());

            ps.executeUpdate();

            UUID generatedId;
            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedId = UUID.fromString(resultSet.getString("id"));
                } else {
                    throw new IllegalStateException("Can`t access to id");
                }
            }
            spend.setId(generatedId);
            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SpendEntity editSpend(SpendEntity spend) {
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE spend SET username = ?, currency = ?," +
                             " spend_date = ?, amount = ?, description = ?, category_id = ? WHERE id = ?")) {
            ps.setString(1, spend.getUsername());
            ps.setString(2, spend.getCurrency().name());

            ps.setDate(3, new Date(spend.getSpendDate().getTime()));

            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategoryId());
            ps.setObject(7, spend.getId());
            ps.executeUpdate();
            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeSpend(SpendEntity spend) {
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM spend WHERE id = ?")) {
            ps.setObject(1, spend.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        List<SpendEntity> entities = new ArrayList<>();
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement userPs = conn.prepareStatement(
                     "SELECT * FROM \"spend\" WHERE username = ?"
             )) {
            userPs.setObject(1, username);
            try (ResultSet resultSet = userPs.executeQuery()) {
                SpendEntity entity = new SpendEntity();
                while (resultSet.next()) {
                    entity.setId(UUID.fromString(resultSet.getString("id")));
                    entity.setUsername(username);
                    entity.setSpendDate(resultSet.getDate("date"));
                    entity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                    entity.setAmount(resultSet.getDouble("amount"));
                    entity.setDescription(resultSet.getString("description"));
                    entity.setCategoryId(UUID.fromString(resultSet.getString("category_id")));
                    entities.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }
}
