package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class SpendEntity {

	private UUID id;

	private String username;

	private Date spendDate;

	private CurrencyValues currency;

	private Double amount;

	private String description;

	private UUID categoryId;

	public static SpendEntity fromJson(SpendJson spendJson, CategoryEntity category) {
		SpendEntity spend = new SpendEntity();
		spend.setId(spendJson.id());
		spend.setUsername(spendJson.username());
		spend.setSpendDate(spendJson.spendDate());
		spend.setCurrency(spendJson.currency());
		spend.setAmount(spendJson.amount());
		spend.setDescription(spendJson.description());
		spend.setCategoryId(category.getId());
		return spend;
	}
}
