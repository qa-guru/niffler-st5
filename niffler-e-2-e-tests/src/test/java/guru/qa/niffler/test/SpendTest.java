package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.ui.page.StartPage;
import org.junit.jupiter.api.Test;

public class SpendTest extends BaseWebTest {


    @Test
    @GenerateCategory(category = "Развлечение", username = "spender")
    void createSpendTest() {
        SpendJson spendJson = SpendJson.randomSpend("Развлечение", "spender");

        Selenide.open(StartPage.URL, StartPage.class)
                .clickLogin()
                .setUsername("spender")
                .setPassword("spender_123")
                .clickSignIn()
                .chooseCategory(spendJson.category())
                .setAmount(spendJson.amount())
                .setDescription(spendJson.description())
                .createSpend()
                .assertThatHistoryContainsSpend(spendJson);
    }

}