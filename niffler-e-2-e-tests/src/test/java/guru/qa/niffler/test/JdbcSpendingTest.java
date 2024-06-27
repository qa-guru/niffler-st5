package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.extension.category.JdbcCategoryExtension;
import guru.qa.niffler.jupiter.extension.spend.JdbcSpendExtension;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.ui.UiBot;
import guru.qa.niffler.ui.page.StartPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.model.CurrencyValues.RUB;

@ExtendWith({
        JdbcCategoryExtension.class,
        JdbcSpendExtension.class
})
public class JdbcSpendingTest {

    private final UiBot ui = new UiBot();

    private final static String USERNAME = "tester";

    static {
        Configuration.browserSize = "1920x1080";
    }

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
    @GenerateSpend(description = "QA.GURU Advanced 5", amount = 65000.00, currency = RUB)
    void spendingShouldBeDeletedAfterTableAction(SpendJson spendJson) {
        ui.mainPage()
                .chooseSpending(spendJson)
                .deleteChosenSpending()
                .assertThatTableContentHasSize(0);
    }

}