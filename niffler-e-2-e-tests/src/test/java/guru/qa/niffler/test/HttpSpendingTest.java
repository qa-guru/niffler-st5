package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.ui.page.StartPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.model.CurrencyValues.RUB;


public class HttpSpendingTest extends BaseWebTest {

    private final static String USERNAME = "zhanna1";

    @BeforeEach
    void doLogin() {
        Selenide.open(StartPage.URL, StartPage.class)
                .clickLogin()
                .setUsername(USERNAME)
                .setPassword("test")
                .clickSignIn();
    }

    @Test
    @GenerateCategory(category = "Обучение", username = USERNAME)
    @GenerateSpend(description = "QA.GURU Advanced 5", amount = 65000.00, currency = RUB)
    void spendingShouldBeDeletedAfterTableAction(SpendJson spendJson) {
        ui.mainPage()
                .chooseSpending(spendJson)
                .deleteChosenSpending()
                .assertThatTableContentHasSize(0);
    }

}
