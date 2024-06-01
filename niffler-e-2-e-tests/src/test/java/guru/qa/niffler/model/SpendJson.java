package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.SpendEntity;

import java.util.Date;
import java.util.UUID;

public record SpendJson(

        @JsonProperty("id")
        UUID id,

        @JsonProperty("spendDate")
        Date spendDate,

        @JsonProperty("category")
        String category,

        @JsonProperty("currency")
        CurrencyValues currency,

        @JsonProperty("amount")
        Double amount,

        @JsonProperty("description")
        String description,

        @JsonProperty("username")
        String username,

        @JsonIgnore
        UUID categoryId) {

    public static SpendJson fromEntity(SpendEntity spendEntity) {
        return new SpendJson(
                spendEntity.getId(),
                spendEntity.getSpendDate(),
                spendEntity.getCategory().getCategory(),
                spendEntity.getCurrency(),
                spendEntity.getAmount(),
                spendEntity.getDescription(),
                spendEntity.getUsername(),
                spendEntity.getCategory().getId()
        );
    }

}