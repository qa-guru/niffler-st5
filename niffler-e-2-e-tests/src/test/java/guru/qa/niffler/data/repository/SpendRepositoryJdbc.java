package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.model.CurrencyValues;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class SpendRepositoryJdbc implements SpendRepository {

    private static final DataSource spendDateSource = DataSourceProvider.dataSource(DataBase.SPEND);

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        try (Connection connection = spendDateSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO \"category\" (category, username) VALUES (?,?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            ps.setString(1, category.getCategory());
            ps.setString(2, category.getUsername());
            ps.executeUpdate();

            UUID generateId;
            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generateId = UUID.fromString(resultSet.getString("id"));

                } else {
                    throw new IllegalAccessException("can't access ");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            category.setId(generateId);
            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        try (Connection connection = spendDateSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "DELETE FROM \"category\" WHERE id = ?"
             )) {
            ps.setObject(1, category.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoryEntity editCategory(CategoryEntity category) {
        try (Connection connection = spendDateSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "UPDATE \"category\" SET category = ?, username = ?) where id = ?",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            ps.setString(1, category.getCategory());
            ps.setString(2, category.getUsername());
            ps.setString(3, category.getId().toString());
            ps.executeUpdate();

            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        try (Connection connection = spendDateSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO spend(username, spend_date, currency, amount, description, category_id) VALUES (?, ?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            ps.setString(1, spend.getUsername());
            ps.setObject(2, new java.sql.Date(spend.getSpendDate().getTime()));
            ps.setObject(3, spend.getCurrency().name());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategory().getId());
            ps.executeUpdate();
            UUID generatedId;
            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedId = UUID.fromString(resultSet.getString("id"));
                } else throw new IllegalStateException("can't access to id");
            }
            spend.setId(generatedId);
            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SpendEntity editSpend(SpendEntity spend) {
        try (Connection connection = spendDateSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "UPDATE spend SET username=?, spend_date=?, currency=?, amount=?, description =?, category_id=? WHERE id=?")) {
            ps.setString(1, spend.getUsername());
            ps.setObject(2, new java.sql.Date(spend.getSpendDate().getTime()));
            ps.setObject(3, spend.getCurrency().name());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategory());
            ps.setObject(7, spend.getId());
            ps.executeUpdate();
            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        try (Connection connection = spendDateSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM spend WHERE username = ?")) {
            ps.setString(1, username);

            ResultSet resultSet = ps.executeQuery();

            List<SpendEntity> spendEntities = new java.util.ArrayList<>();
            while (resultSet.next()) {
                SpendEntity spend = new SpendEntity();
                spend.setId(UUID.fromString(resultSet.getString("id")));
                spend.setUsername(resultSet.getString("username"));
                spend.setSpendDate(resultSet.getDate("spend_date"));
                spend.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                spend.setAmount(resultSet.getDouble("amount"));
                spend.setDescription(resultSet.getString("description"));
                CategoryEntity category = new CategoryEntity();
                category.setId(UUID.fromString(resultSet.getString("category_id")));
                spend.setCategory(category);
                spendEntities.add(spend);
            }
            return spendEntities;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoryEntity findByUsernameAndCategory(String username, String category) {
        try (Connection connection = spendDateSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM category WHERE username = ? AND category = ?")) {
            ps.setString(1, username);
            ps.setString(2, category);

            ResultSet resultSet = ps.executeQuery();

            CategoryEntity categoryEntity = new CategoryEntity();
            resultSet.next(); {
                categoryEntity.setId(UUID.fromString(resultSet.getString("id")));
                categoryEntity.setCategory(resultSet.getString("category"));
                categoryEntity.setUsername(resultSet.getString("username"));
            }
            return categoryEntity;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeSpend(SpendEntity spend) {
        try (Connection connection = spendDateSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "DELETE FROM spend WHERE id = ?")) {
            ps.setObject(1, spend.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
