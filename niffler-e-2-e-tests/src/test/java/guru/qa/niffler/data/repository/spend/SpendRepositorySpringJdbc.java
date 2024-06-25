package guru.qa.niffler.data.repository.spend;

import guru.qa.niffler.data.Database;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.data.sjdbc.SpendEntityRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SpendRepositorySpringJdbc implements SpendRepository {

    private static final JdbcTemplate jdbcTemplate = new JdbcTemplate(
            DataSourceProvider.dataSource(Database.SPEND)
    );

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(
                            "INSERT INTO category (category, username) VALUES (?, ?)",
                            RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, category.getCategory());
                    preparedStatement.setString(2, category.getUsername());
                    return preparedStatement;
                }, keyHolder
        );

        category.setId(UUID.fromString((String) Objects.requireNonNull(keyHolder.getKeys()).get("id")));

        return category;
    }

    @Override
    public List<CategoryEntity> findAllByCategoryName(String categoryName) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT * FROM category WHERE category = ?", categoryName
        );

        return rows.stream().map(
                        row -> {
                            CategoryEntity category = new CategoryEntity();
                            category.setCategory((String) row.get("category"));
                            category.setUsername((String) row.get("username"));
                            category.setId((UUID) row.get("id"));
                            return category;
                        }
                )
                .toList();
    }

    @Override
    public CategoryEntity editCategory(CategoryEntity category) {
        jdbcTemplate.update("UPDATE category SET category = ?, username = ? WHERE id = ?",
                category.getCategory(),
                category.getUsername(),
                category.getId());

        return category;
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        jdbcTemplate.update("DELETE FROM category WHERE id = ?",
                category.getId());
    }

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(
                            """
                                    INSERT INTO spend (
                                    username,
                                    spend_date,
                                    currency,
                                    amount,
                                    description,
                                    category_id)
                                    VALUES (?, ?, ?, ?, ?, ?)
                                    """,
                            RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, spend.getUsername());
                    preparedStatement.setDate(2, new Date(spend.getSpendDate().getTime()));
                    preparedStatement.setString(3, spend.getCurrency().name());
                    preparedStatement.setDouble(4, spend.getAmount());
                    preparedStatement.setString(5, spend.getDescription());
                    preparedStatement.setObject(6, spend.getCategory().getId());
                    return preparedStatement;
                }, keyHolder
        );

        spend.setId((UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id"));

        return spend;
    }

    @Override
    public SpendEntity editSpend(SpendEntity spend) {
        jdbcTemplate.update("""
                        UPDATE spend
                        SET username = ?,
                        currency = ?,
                        spend_date = ?,
                        amount = ?,
                        description = ?,
                        category_id = ?
                        WHERE id = ?
                        """,
                spend.getUsername(),
                spend.getCurrency().name(),
                new Date(spend.getSpendDate().getTime()),
                spend.getAmount(),
                spend.getDescription(),
                spend.getCategory().getId());

        return spend;
    }

    @Override
    public void removeSpend(SpendEntity spend) {
        jdbcTemplate.update("DELETE FROM spend WHERE id = ?",
                spend.getId());
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM spend WHERE username = ?",
                SpendEntityRowMapper.INSTANCE,
                username);
    }

    @Override
    public SpendEntity findAByUsernameAndDescription(String username, String description) {
        return jdbcTemplate.queryForObject("SELECT * FROM spend WHERE username = ? AND description = ?",
                SpendEntityRowMapper.INSTANCE,
                username, description);
    }

}