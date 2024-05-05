package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.jdbc.DataSourceProider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public record CategoryJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("category")
        String category,
        @JsonProperty("username")
        String username) {

        public static CategoryJson fromEntity(CategoryEntity entity) {
                return new CategoryJson(
                        entity.getId(),
                        entity.getCategory(),
                        entity.getUsername()
                );
        }
}
