package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.jdbc.DataSourceProider;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.UUID;

public class RepositoryJdbc implements Repository {
    private static final DataSource spendDataSource = DataSourceProider.dataSource(DataBase.SPEND);

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        try (Connection connection = spendDataSource.getConnection();
             PreparedStatement ps  = connection.prepareStatement(
                     "INSERT INTO \"category\" (category, username)" +
                             "VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getCategory());
            ps.setString(2, category.getUsername());
            ps.executeUpdate();

            UUID generatedId = null;
            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedId = UUID.fromString(resultSet.getString("id"));
                } else {
                    throw new IllegalStateException("");
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return category;
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        try (Connection connection = spendDataSource.getConnection()) {
                 PreparedStatement ps  = connection.prepareStatement(
                         "DELETE FROM \"category\" WHERE ID = ?");
                 {
                     ps.setObject(1, category.getId());
                     ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        try (Connection connection = spendDataSource.getConnection();
             PreparedStatement ps  = connection.prepareStatement(
                     "INSERT INTO \"spend\" (username, spend_date, amount, description, category_id, currency)" +
                             "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, spend.getUsername());
            ps.setDate(2, spend.getSpendDate());
            ps.setDouble(3, spend.getAmount());
            ps.setString(4, spend.getDescription());
            ps.setObject(5, spend.getCategory().getId());
            ps.setString(6, spend.getCurrency().name());
            ps.executeUpdate();

            UUID generatedId = null;
            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedId = UUID.fromString(resultSet.getString("id"));
                } else {
                    throw new IllegalStateException("Illegal state");
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
             PreparedStatement ps = conn.prepareStatement("UPDATE \"spend\" SET username = ?, currency = ?," +
                     " spend_date = ?, amount = ?, description = ?, category_id = ? WHERE id = ?")) {
            ps.setString(1, spend.getUsername());
            ps.setString(2, String.valueOf(spend.getCurrency()));
            ps.setDate(3, new java.sql.Date(spend.getSpendDate().getTime()));
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
    public void removeSpend(SpendEntity spend) {
        try (Connection connection = spendDataSource.getConnection()) {
            PreparedStatement ps  = connection.prepareStatement(
                    "DELETE FROM \"spend\" WHERE ID = ?");
            {
                ps.setObject(1, spend.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
