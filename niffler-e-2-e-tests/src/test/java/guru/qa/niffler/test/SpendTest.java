package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.Spends;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.ui.page.StartPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SpendTest extends BaseWebTest {

    @BeforeEach
    void login() {
        Selenide.open(StartPage.URL, StartPage.class)
                .clickLogin()
                .setUsername("spender")
                .setPassword("spender_123")
                .clickSignIn();
    }


    @Test
    @GenerateCategory(category = "Развлечение", username = "spender")
    void createSpendTest() {
        SpendJson spendJson = SpendJson.randomSpend("Развлечение", "spender");

        ui.mainPage()
                .chooseCategory(spendJson.category())
                .setAmount(spendJson.amount())
                .setDescription(spendJson.description())
                .createSpend()
                .assertThatHistoryContainsSpend(spendJson);
    }

    @Test
    @GenerateCategory(category = "Обучение7", username = "spender")
    @Spends({
            @Spend(
                    description = "Selenide",
                    amount = 10000.99,
                    currency = CurrencyValues.RUB
            ),
            @Spend(
                    description = "Custom Condition",
                    amount = 20000,
                    currency = CurrencyValues.RUB
            )
    })
    void SpendingContentTest(SpendJson[] spendJsons) {
        ui.mainPage()
                .checkSpends(spendJsons);
    }

}