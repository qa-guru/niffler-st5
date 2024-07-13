package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.ui.page.StartPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.model.CurrencyValues.RUB;

public class JdbcSpendingTest extends BaseWebTest {

    private final static String USERNAME = "tester";

    @BeforeEach
    void doLogin() {
        Selenide.open(StartPage.URL, StartPage.class)
                .clickLogin()
                .setUsername(USERNAME)
                .setPassword("qa_pass_666")
                .clickSignIn();
    }

    @Test
    @GenerateCategory(category = "Обучение", username = USERNAME)
    @Spend(description = "QA.GURU Advanced 5", amount = 65000.00, currency = RUB)
    void spendingShouldBeDeletedAfterTableAction(SpendJson spendJson) {
        ui.mainPage()
                .chooseSpending(spendJson)
                .deleteChosenSpending()
                .assertThatTableContentHasSize(0);
    }

}