package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;

import java.util.UUID;

public record SpendJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("spendDate")
        java.sql.Date spendDate,
        @JsonIgnore()
        CategoryEntity categoryEntity,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("amount")
        Double amount,
        @JsonProperty("description")
        String description,
        @JsonProperty("username")
        String username,
        @JsonProperty("category")
                String category)
        {


        public static SpendJson fromEntity(SpendEntity entity) {
                return new SpendJson(
                        entity.getId(),
                        entity.getSpendDate(),
                        entity.getCategory(),
                        entity.getCurrency(),
                        entity.getAmount(),
                        entity.getDescription(),
                        entity.getUsername(),
                        entity.getCategory().getCategory()
                );
        }

}
