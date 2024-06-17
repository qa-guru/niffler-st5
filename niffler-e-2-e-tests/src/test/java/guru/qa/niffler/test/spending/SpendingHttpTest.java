package guru.qa.niffler.test.spending;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.meta.HttpTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

@HttpTest
public class SpendingHttpTest {

    private final MainPage mainPage = new MainPage();
    private final WelcomePage welcomePage = new WelcomePage();
    private final LoginPage loginPage = new LoginPage();

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000");
        welcomePage.goToLogin();
        loginPage.login("dima", "12345");
    }

    @Category(
            category = "Обучение",
            username = "dima")
    @Spend(
            spendDate = "2024-04-18",
            category = "Обучение",
            currency = CurrencyValues.RUB,
            amount = 65000.00,
            description = "QA.GURU Advanced 5",
            username = "dima"
    )
    @DisplayName("Создание spend через Http запрос")
    @Description("Создаётся категория, затем расходник, в тесте проверяется что расходник отображается в списке." +
            " После теста согласно расширению вызывается метод удаления, который ещё не реализован для http," +
            " поэтому категория сохраняется, а расходник удаляется через UI. Для перезапуска указать новую категорию.")
    @Test
    void spendingShouldBeDeletedAfterTableActionWithHttpExt(SpendJson spendJson) {
        mainPage
                .selectSpendingByDescription(spendJson.description())
                .deleteSpending();

        assertFalse(mainPage.checkRow(spendJson.description()),
                "В таблице найдена строка: " + spendJson.description());
    }

}
