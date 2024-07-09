package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.extension.ApiCategoryExtension;
import guru.qa.niffler.jupiter.extension.ApiSpendExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.AuthPage;
import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.pages.StartPage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith({ApiCategoryExtension.class, ApiSpendExtension.class})
public class SpendingTestHttp {

    static {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeAll
    public static void setRepository() {
        System.setProperty("repo", "sjdbc");
    }

    @BeforeEach
    void doLogin() {
        // createSpend

        StartPage startPage = new StartPage();
        AuthPage authPage = new AuthPage();

        startPage.openPage()
        .clickLoginButton();
        authPage.login("Nastiletko", "bB!123456");
    }

    @GenerateSpend(
            description = "QA.GURU Advanced 5",
            amount = 65000.00,
            currency = CurrencyValues.RUB
    )
    @GenerateCategory(
            category = "Обучение",
            username = "Nastiletko"
    )
    @Test
    void spendingShouldBeDeletedAfterTableAction(SpendJson spendJson) {

        MainPage mainPage = new MainPage();
        mainPage.clickCheckbox(spendJson.description())
                .deleteSpending()
                .checkSpendingsDeletedText()
                .checkSpendingsCount(0);
    }
}
