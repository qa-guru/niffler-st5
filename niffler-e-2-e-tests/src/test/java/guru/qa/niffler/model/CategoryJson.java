package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.CategoryEntity;

import java.util.UUID;

public record CategoryJson(

        @JsonProperty("id")
        UUID id,

        @JsonProperty("category")
        String categoryName,

        @JsonProperty("username")
        String username) {

    public static CategoryJson fromEntity(CategoryEntity categoryEntity) {
        return new CategoryJson(
                categoryEntity.getId(),
                categoryEntity.getCategory(),
                categoryEntity.getUsername()
        );
    }

}
