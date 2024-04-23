package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.jupiter.extension.SpendExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.pages.WelcomePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith(CategoryExtension.class)
@ExtendWith(SpendExtension.class)
public class SpendingTest {

    private MainPage mainPage;

    static {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin() {
        mainPage = open("http://127.0.0.1:3000/main", WelcomePage.class)
                .openLoginPage()
                .doLogin();
    }

    @GenerateCategory(
            category = "Обучение",
            username = "dotsarev"
    )

    @GenerateSpend(username = "dotsarev",
            description = "QA.GURU Advanced 5",
            amount = 65000.00,
            currency = CurrencyValues.RUB,
            category = "Обучение")

    @Test
    void spendingShouldBeDeletedAfterTableAction(SpendJson spendJson) {
        final int expectedSize = 0;

        SelenideElement rowWithSpending = mainPage
                .findRowWithSpendingByDescription(spendJson.description());

                mainPage.chooseSpending(rowWithSpending)
                        .deleteSpending()
                        .checkCountOfSpendings(expectedSize);
    }
}
