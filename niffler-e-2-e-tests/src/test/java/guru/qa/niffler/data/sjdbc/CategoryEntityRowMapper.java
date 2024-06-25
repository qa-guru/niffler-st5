package guru.qa.niffler.data.sjdbc;

import guru.qa.niffler.data.entity.CategoryEntity;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CategoryEntityRowMapper implements RowMapper<CategoryEntity> {

    public static final CategoryEntityRowMapper INSTANCE = new CategoryEntityRowMapper();

    @Override
    public CategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setUsername(rs.getString("username"));
        categoryEntity.setCategory(rs.getString("category"));
        categoryEntity.setId(rs.getObject("id", UUID.class));
        return categoryEntity;
    }

}
