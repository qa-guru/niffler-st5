package guru.qa.niffler.data.sjdbc;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SpendEntityRowMapper implements RowMapper<SpendEntity> {

    @Override
    public SpendEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpendEntity spend = new SpendEntity();
        CategoryEntity category = new CategoryEntity();
        category.setId(rs.getObject("category_id", UUID.class));

        spend.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        spend.setId((UUID) rs.getObject("id"));
        spend.setUsername(rs.getString("username"));
        spend.setSpendDate(rs.getDate("spend_date"));
        spend.setAmount(rs.getDouble("amount"));
        spend.setDescription(rs.getString("description"));
        spend.setCategory(category);
        return spend;
    }
}
