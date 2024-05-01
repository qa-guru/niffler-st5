package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.jupiter.extension.SpendExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.AuthPage;
import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.pages.StartPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;


@ExtendWith({CategoryExtension.class, SpendExtension.class})
public class SpendingTest {

    static {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin() {
        // createSpend

        StartPage startPage = new StartPage();
        AuthPage authPage = new AuthPage();

        Selenide.open("http://127.0.0.1:3000/");
        startPage.clickLoginButton();
        authPage.login("dima", "12345");

    }

    @Test
    void anotherTest() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").should(visible);
    }

    @GenerateSpend(
            description = "QA.GURU Advanced 5",
            amount = 65000.00,
            currency = CurrencyValues.RUB
    )
    @GenerateCategory(
            category = "Обучение045",
            username = "dima"
    )
    @Test
    void spendingShouldBeDeletedAfterTableAction(SpendJson spendJson) {

        MainPage mainPage = new MainPage();

        mainPage.clickCheckbox(mainPage.findSpendingByDescription(spendJson.description()))
                .deleteSpending()
                .checkSpendingsDeletedText()
                .checkSpendingsCount(0);


    }
}
