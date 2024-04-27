package guru.qa.niffler.test;

import guru.qa.niffler.extensions.DesktopCapabilities;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pageObjects.LoginPage;
import guru.qa.niffler.pageObjects.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


@ExtendWith(DesktopCapabilities.class)
public class SpendingTest {

    private final String AUTH_PAGE = "http://127.0.0.1:3000/";

    @Category(category = "обучение6", username = "dima")
    @Spend(currency = CurrencyValues.RUB, amount = 65000.0, description = "курсы99")
    @Test
    void spendingShouldBeDeletedAfterTableAction(SpendJson spendJson) {
        open(AUTH_PAGE, LoginPage.class)
                .doLogin()
                .at(MainPage.class)
                .selectCategory(spendJson.category())
                .deleteCategory();
    }
}
