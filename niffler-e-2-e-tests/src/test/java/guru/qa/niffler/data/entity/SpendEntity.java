package guru.qa.niffler.data.entity;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.jdbc.DataSourceProider;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.UUID;

@Setter
@Getter
public class SpendEntity implements Serializable {
    private UUID id;

    private String username;

    private CurrencyValues currency;

    private java.sql.Date spendDate;

    private Double amount;

    private String description;

    private CategoryEntity category;

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public CurrencyValues getCurrency() {
        return currency;
    }

    public java.sql.Date getSpendDate() {
        return spendDate;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCurrency(CurrencyValues currency) {
        this.currency = currency;
    }

    public void setSpendDate(java.sql.Date spendDate) {
        this.spendDate = spendDate;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public static SpendEntity fromJson(SpendJson spendJson) {
        SpendEntity tempEntity = new SpendEntity();
        tempEntity.setId(spendJson.id());
        tempEntity.setUsername(spendJson.username());
        tempEntity.setCurrency(spendJson.currency());
        tempEntity.setSpendDate(spendJson.spendDate());
        tempEntity.setAmount(spendJson.amount());
        tempEntity.setDescription(spendJson.description());
        tempEntity.setCategory(spendJson.categoryEntity());
        return tempEntity;
    }

    public static SpendEntity fromSpend(Spend spend) {
        SpendEntity tempSpend = new SpendEntity();

        CategoryEntity tempCategoryEntity = new CategoryEntity();
        tempCategoryEntity.setUsername(spend.username());
        tempCategoryEntity.setCategory(spend.category().category());
        String categoryId = null;
        try (Connection connection = DataSourceProider.dataSource(DataBase.SPEND).getConnection();
             PreparedStatement ps  = connection.prepareStatement(
                     "select id from category where category = '" + spend.category().category() +
                             "' and username = '" + spend.username() + "'", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.execute();
            try (ResultSet resultSet = ps.getResultSet()) {
                if (resultSet.next()) {
                    categoryId = resultSet.getString("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tempCategoryEntity.setId(UUID.fromString(categoryId));

        tempSpend.setCategory(tempCategoryEntity);
        tempSpend.setUsername(spend.username());
        tempSpend.setAmount(spend.amount());
        tempSpend.setDescription(spend.description());
        tempSpend.setCurrency(spend.currency());
        tempSpend.setSpendDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        return tempSpend;
    }
}

