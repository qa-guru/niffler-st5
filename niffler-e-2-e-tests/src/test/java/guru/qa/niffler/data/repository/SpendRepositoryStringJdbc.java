package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.data.sjdbc.SendEntityRowMapper;
import guru.qa.niffler.model.CategoryJson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SpendRepositoryStringJdbc implements SpendRepository {

	private static final JdbcTemplate jdbcTemplate = new JdbcTemplate(
			DataSourceProvider.dataSource(DataBase.SPEND));

	@Override
	public CategoryEntity createCategory(CategoryEntity category) {
		return null;
	}

	@Override
	public CategoryEntity editCategory(CategoryEntity category) {
		return null;
	}

	@Override
	public void removeCategory(CategoryEntity category) {

	}

	@Override
	public SpendEntity createSpend(SpendEntity spend) {
		KeyHolder kh = new GeneratedKeyHolder();
		jdbcTemplate.update(con -> {
					PreparedStatement ps = con.prepareStatement(
							"INSERT INTO spend (username, currency, spend_date, amount, description, category_id)" +
									" VALUES (?, ?, ?, ?, ?, ?)",
							PreparedStatement.RETURN_GENERATED_KEYS
					);
					ps.setString(1, spend.getUsername());
					ps.setString(2, spend.getCurrency().name());
					ps.setDate(3, new Date(spend.getSpendDate().getTime()));
					ps.setDouble(4, spend.getAmount());
					ps.setString(5, spend.getDescription());
					ps.setObject(6, spend.getCategoryId());
					return ps;
				}, kh
		);
		spend.setId((UUID) Objects.requireNonNull(kh.getKeys()).get("id"));
		return spend;
	}

	@Override
	public SpendEntity editSpend(SpendEntity spend) {
		return null;
	}

	@Override
	public void removeSpend(SpendEntity spend) {

	}

	@Override
	public void removeSpendByCategoryId(CategoryJson category) {

	}

	@Override
	public List<SpendEntity> findAllByUsername(String username) {
		return jdbcTemplate.query("SELECT * FROM public.spend where username = ?;",
				SendEntityRowMapper.instance,
				username);
	}
}
