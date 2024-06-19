package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class SpendEntity implements Serializable {
    private UUID id;
    private String username;
    private CurrencyValues currency;
    private Date spendDate;
    private Double amount;
    private String description;
    private CategoryEntity category;

    public static SpendEntity fromJson(SpendJson spend, CategoryJson category) {
        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setId(spend.id());
        spendEntity.setSpendDate(spend.spendDate());
        spendEntity.setCurrency(spend.currency());
        spendEntity.setAmount(spend.amount());
        spendEntity.setCategory(CategoryEntity.fromJson(category));
        spendEntity.setUsername(category.username());
        spendEntity.setDescription(spend.description());
        return spendEntity;
    }


    public static SpendEntity fromJson(SpendJson spend) {
        SpendEntity spendEntity = new SpendEntity();

        spendEntity.setId(spend.id());
        spendEntity.setSpendDate(spend.spendDate());
        spendEntity.setAmount(spend.amount());
        spendEntity.setDescription(spend.description());
        return spendEntity;
    }
}
