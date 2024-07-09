package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;

import java.util.List;


public class ApiCategoryExtension extends AbstractCategoryExtension {

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    protected CategoryJson createCategory(CategoryJson category) throws Exception {
        return spendApiClient.createCategory(category);
    }

    @Override
    protected void removeCategory(CategoryJson json) {
        spendApiClient.removeCategory(List.of(String.valueOf(json.id())));
    }

}
