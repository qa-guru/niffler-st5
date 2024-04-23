package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.extension.SpendExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.getFocusedElement;

@ExtendWith(SpendExtension.class)
public class SpendingTest {
    LoginPage loginPage = new LoginPage();
    MainPage mainPage = new MainPage();
    WelcomePage welcomePage = new WelcomePage();

    static {
        Configuration.browserSize = "2560x1080";
    }

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        welcomePage.waitPageLoaded().doLoginRedirect();
        loginPage.fillLoginPass("ibutov", "12345")
                .clickSubmit();
    }

    @Spend(username = "ibutov",
            description = "QA.GURU Advanced 5",
            amount = 65000.00,
            currency = CurrencyValues.RUB,
            category = "Обучение")
    @Test
    void spendingShouldBeDeletedAfterTableAction(SpendJson spendJson) {
        SelenideElement rowWithSpending = mainPage.findSpendingRowByDescription(spendJson.description());
        mainPage.chooseSpending(rowWithSpending)
                .deleteSpending()
                .checkSpendingShouldBeDeletedAfterTableAction(0);
    }
}
