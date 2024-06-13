package guru.qa.niffler.data.sjdbc;

import guru.qa.niffler.data.entity.CategoryEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CategoryEntityRowMapper implements RowMapper<CategoryEntity> {

    // Static field для хранения единственного экземпляра класса
    public static final CategoryEntityRowMapper instance = new CategoryEntityRowMapper();

    @Override
    public CategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId((UUID) rs.getObject("id"));
        categoryEntity.setCategory(rs.getString("category"));
        categoryEntity.setUsername(rs.getString("username"));
        return categoryEntity;
    }
}
