package guru.qa.niffler.test.spending;

import com.codeborne.selenide.ElementsCollection;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.Spends;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@WebTest
public class SpendingsApiTest extends BaseWebTest {

    private final String CATEGORY = "001_Обучение_999";
    private final String USER = "dorko";

    @BeforeEach
    public void openLoginPage() {
        open(CFG.frontUrl());
        welcomePage.goToLogin();
    }

    @Category(
            category = CATEGORY,
            username = USER
    )
    @Spends({
            @Spend(
                    spendDate = "2024-04-18",
                    category = CATEGORY,
                    currency = CurrencyValues.RUB,
                    amount = 65000.00,
                    description = "01_QA.GURU Advanced 5 - Обучение",
                    username = USER
            ),
            @Spend(
                    spendDate = "2024-04-18",
                    category = CATEGORY,
                    currency = CurrencyValues.RUB,
                    amount = 15000.00,
                    description = "02_QA.GURU Advanced 5 - Диплом",
                    username = USER
            )
    })
    @DisplayName("Создание spends по API")
    @Test
    void checkSpendingContent(SpendJson[] spends) {
        loginPage.login(USER, "12345");

        ElementsCollection spendings = $(".spendings-table tbody")
                .$$("tr");
        int i = 5;
        //spendings.shouldHave(spendsInTable(spends));
    }

}
