package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import lombok.SneakyThrows;

public class ApiCategoryExtension extends AbstractCategoryExtension {

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    @SneakyThrows
    protected CategoryJson createCategory(Category category) {
        CategoryJson categoryJson = new CategoryJson(null, category.username(), category.category());
        return spendApiClient.createCategory(categoryJson);
    }

    @Override
    protected void removeCategory(CategoryJson category) {
        throw new UnsupportedOperationException();
    }
}
