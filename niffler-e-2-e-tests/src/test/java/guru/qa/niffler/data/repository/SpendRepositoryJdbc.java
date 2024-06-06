package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.CurrencyValues;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class SpendRepositoryJdbc implements SpendRepository {

    private static final DataSource spendDataSource = DataSourceProvider.dataSource(DataBase.SPEND);

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO \"category\" (category, username) VALUES (?, ?)",
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
             PreparedStatement ps = conn.prepareStatement("UPDATE \"category\" SET category = ?, username = ? WHERE id = ?")) {
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
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        CategoryEntity category = new CategoryEntity();
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("SELECT * FROM \"category\" WHERE id = ? ")) {
            usersPs.setObject(1, id);

            usersPs.execute();
            ResultSet resultSet = usersPs.getResultSet();

            if (resultSet.next()) {
                category.setId(resultSet.getObject("id", UUID.class));
                category.setUsername(resultSet.getString("username"));
                category.setCategory(resultSet.getString("category"));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(category);
    }

    @Override
    public Optional<CategoryEntity> findUserCategoryByName(String username, String category) {
        CategoryEntity result = new CategoryEntity();
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("SELECT * FROM \"category\" WHERE \"category\".category = ? and username = ?")) {
            usersPs.setString(1, category);
            usersPs.setString(2, username);

            usersPs.execute();
            ResultSet resultSet = usersPs.getResultSet();

            if (resultSet.next()) {
                result.setId(resultSet.getObject("id", UUID.class));
                result.setUsername(resultSet.getString("username"));
                result.setCategory(resultSet.getString("category"));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(result);
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
            ps.setObject(6, spend.getCategory().getId());

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
            ps.setObject(6, spend.getCategory().getId());
            ps.setObject(7, spend.getId());
            ps.executeUpdate();
            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        SpendEntity spend = new SpendEntity();
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("SELECT * FROM spend WHERE id = ? ")) {
            usersPs.setObject(1, id);

            usersPs.execute();
            ResultSet resultSet = usersPs.getResultSet();

            if (resultSet.next()) {
                spend.setId(resultSet.getObject("id", UUID.class));
                spend.setUsername(resultSet.getString("username"));
                spend.setSpendDate(resultSet.getDate("spend_date"));
                spend.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                spend.setAmount(resultSet.getDouble("amount"));
                spend.setDescription(resultSet.getString("description"));
                spend.setCategory(
                        findCategoryById(
                                resultSet.getObject("category_id", UUID.class)
                        ).orElseThrow()
                );
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(spend);
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
}
