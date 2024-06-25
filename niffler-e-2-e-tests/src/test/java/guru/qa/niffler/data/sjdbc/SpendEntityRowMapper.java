package guru.qa.niffler.data.sjdbc;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class SpendEntityRowMapper implements RowMapper<SpendEntity> {

    public static final SpendEntityRowMapper INSTANCE = new SpendEntityRowMapper();

    @Override
    public SpendEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpendEntity spend = new SpendEntity();
        CategoryEntity category = new CategoryEntity();
        category.setId(rs.getObject("category_id", UUID.class));
        category.setUsername(rs.getString("username"));
        category.setCategory(rs.getString("category"));

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
