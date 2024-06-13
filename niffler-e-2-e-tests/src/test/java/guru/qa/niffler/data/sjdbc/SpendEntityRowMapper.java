package guru.qa.niffler.data.sjdbc;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepositorySpringJdbc;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SpendEntityRowMapper implements RowMapper<SpendEntity> {

    // Static field для хранения единственного экземпляра класса
    public static final SpendEntityRowMapper instance = new SpendEntityRowMapper();

    // Private constructor для предотвращения создания экземпляров класса
    private SpendEntityRowMapper() {
    }

    SpendRepositorySpringJdbc spendRepository = new SpendRepositorySpringJdbc();

    @Override
    public SpendEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpendEntity spend = new SpendEntity();
        spend.setId(UUID.fromString(rs.getString("id")));
        spend.setUsername(rs.getString("username"));
        spend.setSpendDate(rs.getDate("spend_date"));
        spend.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        spend.setAmount(rs.getDouble("amount"));
        spend.setDescription(rs.getString("description"));
        spend.setCategory(spendRepository.getCategoryEntityById(UUID.fromString(rs.getString("category_id"))).get());
        return spend;
    }
}