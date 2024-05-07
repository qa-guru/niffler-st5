package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.extension.GenerateCategoryExtension;
import guru.qa.niffler.jupiter.extension.GenerateSpendExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.authentication.AuthorizationPage;
import guru.qa.niffler.pages.main.MainPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({GenerateCategoryExtension.class,
        GenerateSpendExtension.class})
public class SpendingTest {
    private final AuthorizationPage authorizationPage = new AuthorizationPage();

    private final MainPage mainPage = new MainPage();

    static {
        Configuration.browserSize = "1920x1080";

    }

    @BeforeEach
    void doLogin() {
        // createSpend
        Selenide.open("http://127.0.0.1:3000/");

        authorizationPage.clickLogInButton()
                .setUsername("testuser")
                .setPassword("Voisjf%05842")
                .clickSignInButton();

    }

    @GenerateCategory(
            category = "Обучение",
            username = "testuser"
    )
    @GenerateSpend(
            username = "testuser",
            description = "QA.GURU Advanced 5",
            amount = 65000.00,
            currency = CurrencyValues.RUB,
            category = "Обучение"
    )
    @Test
    void spendingShouldBeDeletedAfterTableAction(SpendJson spendJson) {
        mainPage.choosingFirstSpending(mainPage
                        .findSpendingByDescription(spendJson.description()))
                .clickDeleteSelected()
                .expectedTableSize(0);
    }
}
