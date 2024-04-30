package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.LoginPage;
import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.pages.WelcomePage;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;

@WebTest
public class SpendingTest {

    private MainPage mainPage = new MainPage();
    private WelcomePage welcomePage = new WelcomePage();
    private LoginPage loginPage = new LoginPage();

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000");
        welcomePage.goToLogin();
        loginPage.login("den", "123");
    }

    @AfterEach
    void doScreenshot() {
        Allure.addAttachment(
                "Screen on test end",
                new ByteArrayInputStream(
                        Objects.requireNonNull(
                                Selenide.screenshot(OutputType.BYTES)
                        )
                )
        );
    }

    @Category(
            category = "Обучение",
            username = "den")
    @Spend(
            spendDate = "currentDate",
            category = "Обучение",
            currency = CurrencyValues.RUB,
            amount = 65000.00,
            description = "QA.GURU Advanced 5",
            username = "den"
    )
    @Test
    void spendingShouldBeDeletedAfterTableAction(SpendJson spendJson) {
        mainPage
                .selectSpendingByDescription(spendJson.description())
                .deleteSpending();

        assertFalse(mainPage.checkRow(spendJson.description()),
                "В таблице найдена строка: " + spendJson.description());
    }
}
