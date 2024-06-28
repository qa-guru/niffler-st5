package guru.qa.niffler.data.sjdbc;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SpendEntityRowMapper implements RowMapper<SpendEntity> {

    public static final SpendEntityRowMapper instance = new SpendEntityRowMapper();

    private SpendEntityRowMapper() {

    }

    @Override
    public SpendEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setUsername(rs.getString("username"));
        spendEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        spendEntity.setId((UUID) rs.getObject("id"));
        spendEntity.setSpendDate(rs.getDate("spend_date"));
        spendEntity.setAmount(rs.getDouble("amount"));
        spendEntity.setDescription(rs.getString("description"));
        CategoryEntity category = new CategoryEntity();
        category.setId(UUID.fromString(rs.getString("category_id")));
        spendEntity.setCategory(category);
        return spendEntity;
    }
}
