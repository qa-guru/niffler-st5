package guru.qa.niffler.data.entity;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class CategoryEntity implements Serializable {
    private UUID id;

    private String category;

    private String username;

    public String getCategory() {
        return category;
    }

    public String getUsername() {
        return username;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static CategoryEntity fromJson(CategoryJson categoryJson) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(categoryJson.id());
        entity.setCategory(categoryJson.category());
        entity.setUsername(categoryJson.username());
        return entity;
    }

    public static CategoryEntity fromCategory(Category category) {
        CategoryEntity entity = new CategoryEntity();
        entity.setCategory(category.category());
        entity.setUsername(category.username());
        return entity;
    }
}
