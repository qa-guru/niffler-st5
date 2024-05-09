package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.extension.SpendExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.LandingPage;
import guru.qa.niffler.pages.LoginPage;
import guru.qa.niffler.pages.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SpendExtension.class)
public class SpendingTest {

    static {
        Configuration.browserSize = "1920x1080";
    }

    private final LandingPage landingPage = new LandingPage();
    private final LoginPage loginPage = new LoginPage();
    private final MainPage mainPage = new MainPage();


        @Spend(
            username = "dima",
            description = "QA.GURU Advanced 5",
            amount = 65000.00,
            currency = CurrencyValues.RUB,
            category = "Обучение"
    )
    @Test
    void spendingShouldBeDeletedAfterTableActionUsePages(SpendJson spendJson) {
        Selenide.open("http://127.0.0.1:3000/");
        landingPage.clickLogin();
        loginPage.setUsername("dima")
            .setPassword("12345")
            .signIn();

        SelenideElement rowWithSpending = mainPage.findSpendingRowByDescription(spendJson.description());
        mainPage.chooseSpending(rowWithSpending)
                .deleteSpending()
                .checkCountOfSpendings(0);
    }
}
