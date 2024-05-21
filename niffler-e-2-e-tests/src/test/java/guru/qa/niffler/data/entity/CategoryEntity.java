package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.CategoryJson;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryEntity {

	private UUID id;
	private String category;
	private String username;

	public static CategoryEntity fromJson(CategoryJson categoryJson){
		CategoryEntity categoryEntity = new CategoryEntity();
		categoryEntity.setId(categoryJson.id());
		categoryEntity.setCategory(categoryJson.category());
		categoryEntity.setUsername(categoryJson.username());
		return categoryEntity;
	}




}
