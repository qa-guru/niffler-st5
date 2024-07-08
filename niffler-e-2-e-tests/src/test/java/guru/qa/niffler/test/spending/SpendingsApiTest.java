package guru.qa.niffler.test.spending;

import com.codeborne.selenide.ElementsCollection;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.Spends;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.test.BaseWebTest;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@WebTest
@Epic("Spends")
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
                    amount = 65000.99,
                    description = "01_QA.GURU Advanced 5 - Обучение",
                    username = USER
            ),
            @Spend(
                    spendDate = "2024-04-18",
                    category = CATEGORY,
                    currency = CurrencyValues.RUB,
                    amount = 65000.99,
                    description = "01_QA.GURU Advanced 5 - Экзамен",
                    username = USER
            ),
            @Spend(
                    spendDate = "2024-04-18",
                    category = CATEGORY,
                    currency = CurrencyValues.RUB,
                    amount = 15000.55,
                    description = "02_QA.GURU Advanced 5 - Диплом",
                    username = USER
            )
    })
    @DisplayName("Создание spends по API и проверка в таблице")
    @Test
    void checkSpendingContent(SpendJson[] spends) {
        loginPage.login(USER, "12345");

        mainPage.getSpendingsTable().shouldHaveSpendings(spends);

        //для теста меняю ожидаемое значение в двух элементах, чтобы получить ошибку
        int[] indices = {0, 1};
        mainPage.getSpendingsTable().shouldHaveSpendings(updateSpendDescriptions(spends, indices, "wrong_description"));
    }

    public static SpendJson[] updateSpendDescriptions(SpendJson[] spends, int[] indices, String newDescription) {
        SpendJson[] updatedSpends = Arrays.copyOf(spends, spends.length);
        for (int index : indices) {
            SpendJson spend = updatedSpends[index];
            updatedSpends[index] = new SpendJson(
                    spend.id(),
                    spend.spendDate(),
                    spend.category(),
                    spend.currency(),
                    spend.amount(),
                    newDescription,
                    spend.username()
            );
        }
        return updatedSpends;
    }

}
