package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;
import lombok.SneakyThrows;

public class ApiCategoryExtension extends CategoryExtension {

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    @SneakyThrows
    protected CategoryJson createCategory(CategoryJson category) {
        return spendApiClient.createCategory(category);
    }

    @Override
    protected void removeCategory(CategoryJson spend) {
        throw new UnsupportedOperationException();
    }
}
