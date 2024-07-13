package guru.qa.niffler.jupiter.extension.category;

import guru.qa.niffler.api.spend.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

public class HttpCategoryExtension extends AbstractCategoryExtension {

    private final SpendApiClient spendApi = new SpendApiClient();

    @Override
    protected CategoryJson createCategory(CategoryJson categoryJson) {
        try {
            return requireNonNull(spendApi.getCategories(categoryJson.username())).stream()
                    .filter(cat -> cat.username().equals(categoryJson.username()) &&
                                    cat.categoryName().equals(categoryJson.categoryName()))
                    .findAny()
                    .orElse(spendApi.createCategory(categoryJson));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void removeCategory(CategoryJson categoryJson) {

    }

}