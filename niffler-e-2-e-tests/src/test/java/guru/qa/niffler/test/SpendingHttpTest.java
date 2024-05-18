package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.meta.WebTestHttp;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
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

@WebTestHttp
public class SpendingHttpTest {
    static {
        Configuration.browserSize = "1920x1080";
    }

    private final WelcomePage welcomePage = new WelcomePage();
    private final LoginPage loginPage = new LoginPage();
    private final MainPage mainPage = new MainPage();

    @BeforeEach
    void doLogin() {
        // createSpend
        Selenide.open("http://127.0.0.1:3000/");
        UserJson dima = UserJson.simpleUser("dima1", "cat1");
        Selenide.open("http://127.0.0.1:3000/");
        welcomePage.loginBtnClick();
        loginPage.fillLoginPage(dima);
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
            category = "Обучение361",
            username = "dima1"
    )
    @Spend(
            description = "QA.GURU Advanced 5",
            amount = 65000.00,
            currency = CurrencyValues.RUB,
            category = @Category(
                    category = "Обучение361",
                    username = "dima1"
            ),
            username = "dima1")

    @Test
    void spendingShouldBeDeletedAfterTableAction(CategoryJson categoryJson, SpendJson spendJson) {
        mainPage.spendingSizeShouldBe(1); //т.к для данного апи нету метода удаления тест из jdbc пакета может падать т.к непонятно сколько к этому времени спендов создано
    }
}
