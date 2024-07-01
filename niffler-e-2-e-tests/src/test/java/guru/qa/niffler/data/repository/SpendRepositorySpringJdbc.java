package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.data.sjdbc.CategoryEntityRowMapper;
import guru.qa.niffler.data.sjdbc.SpendEntityRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.UUID;

public class SpendRepositorySpringJdbc implements SpendRepository {

    private static final JdbcTemplate jdbcTemplate = new JdbcTemplate(
            DataSourceProvider.dataSource(DataBase.SPEND)
    );

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {

        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO \"category\" (category, username) VALUES (?,?)",
                            PreparedStatement.RETURN_GENERATED_KEYS);
                    ps.setString(1, category.getCategory());
                    ps.setString(2, category.getUsername());
                    return ps;
                }, kh
        );
        category.setId((UUID) kh.getKeys().get("id"));
        return category;

    }

    @Override
    public void removeCategory(CategoryEntity category) {
        jdbcTemplate.update("DELETE FROM \"category\" WHERE id = ?",
                category.getId()
        );
    }

    @Override
    public CategoryEntity editCategory(CategoryEntity category) {

        jdbcTemplate.update("UPDATE \"category\" SET category = ?, username = ?) where id = ?",
                category.getCategory(),
                category.getUsername(),
                category.getId().toString()
        );
        return category;
    }

    @Override
    public SpendEntity createSpend(SpendEntity spend) {

        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO spend(username, spend_date, currency, amount, description, category_id) VALUES (?, ?, ?, ?, ?, ?)",
                            PreparedStatement.RETURN_GENERATED_KEYS);
                    ps.setString(1, spend.getUsername());
                    ps.setObject(2, new java.sql.Date(spend.getSpendDate().getTime()));
                    ps.setObject(3, spend.getCurrency().name());
                    ps.setDouble(4, spend.getAmount());
                    ps.setString(5, spend.getDescription());
                    ps.setObject(6, spend.getCategory().getId());
                    return ps;
                }, kh
        );
        spend.setId((UUID) kh.getKeys().get("id"));
        return spend;
    }

    @Override
    public SpendEntity editSpend(SpendEntity spend) {
        jdbcTemplate.update("UPDATE spend SET username=?, spend_date=?, currency=?, " +
                        "amount=?, description =?, category_id=? WHERE id=?",
                spend.getUsername(),
                new java.sql.Date(spend.getSpendDate().getTime()),
                spend.getCurrency().name(),
                spend.getAmount(),
                spend.getDescription(),
                spend.getCategory(),
                spend.getId()
        );
        return spend;
    }

    @Override
    public void removeSpend(SpendEntity spend) {
        jdbcTemplate.update("DELETE FROM spend WHERE id = ?",
                spend.getId()
        );
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        return jdbcTemplate.query(
                "SELECT * FROM spend WHERE username = ?",
                SpendEntityRowMapper.instance, username);
    }

    @Override
    public CategoryEntity findByUsernameAndCategory(String username, String category) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM category WHERE username = ? and category = ?",
                CategoryEntityRowMapper.instance, username, category);
    }
}
