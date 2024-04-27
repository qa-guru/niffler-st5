package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.retrofit.categoriesEndpoint.CategoriesClient;
import guru.qa.niffler.retrofit.spendsEndpoint.SpendsClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class PrepareTestData implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        new CategoriesClient().addNewCategory("Аквадискотека", "dima");
        new SpendsClient().createSpend("Аквадискотека", CurrencyValues.RUB, 65000.00, "хорошо поплавали", "dima");
    }
}
