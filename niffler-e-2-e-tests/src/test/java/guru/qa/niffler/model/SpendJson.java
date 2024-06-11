package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.SpendEntity;

import java.util.Date;
import java.util.UUID;

public record SpendJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("spendDate")
        Date spendDate,
        @JsonProperty("amount")
        Double amount,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("category")
        String category,
        @JsonProperty("description")
        String description,
        @JsonProperty("username")
        String username) {

    public SpendJson addUsername(String username) {
        return new SpendJson(id, spendDate, amount, currency, category, description, username);
    }

    public static SpendJson fromEntity(SpendEntity entity) {
        return new SpendJson(
                entity.getId(),
                entity.getSpendDate(),
                entity.getAmount(),
                CurrencyValues.valueOf(entity.getCurrency().name()),
                entity.getCategory().getCategory(),
                entity.getDescription(),
                entity.getUsername()
        );
    }
}
