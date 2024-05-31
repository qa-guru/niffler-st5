package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;
import guru.qa.niffler.data.entity.CategoryEntity;

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

	public static CategoryJson randomByUsername(String username) {
		return new CategoryJson(
				null,
				new Faker().job().field(),
				username
		);
	}
}
