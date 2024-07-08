package guru.qa.niffler.test.parallelWeb;

import guru.qa.niffler.jupiter.annotation.DbUser;
import guru.qa.niffler.jupiter.extension.DbCreateUserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.BaseWebTest;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;

@Epic("Spendings")
@Tag("ParallelWebTests")
@ExtendWith(DbCreateUserExtension.class)
public class SpendingsTest extends BaseWebTest {

    @BeforeEach
    public void openLoginPage() {
        open(CFG.frontUrl());
        welcomePage.goToLogin();
    }

    @Test
    @DbUser
    @DisplayName("Проверка списка трат")
    void spendsListTest(UserJson userJson) {

        List<String> spends = new ArrayList<>();

        CategoryJson category = spendApiClient.createCategory(new CategoryJson(
                null, "Обучение", userJson.username()));

        spends.add(spendApiClient.createSpend(new SpendJson(
                        null, new Date(), category.category(), CurrencyValues.RUB,
                        60000.00, "SpendDescription_1", userJson.username()))
                .description());

        spends.add(spendApiClient.createSpend(new SpendJson(
                        null, new Date(), category.category(), CurrencyValues.RUB,
                        90000.00, "SpendDescription_2", userJson.username()))
                .description());

        spends.add(spendApiClient.createSpend(new SpendJson(
                        null, new Date(), category.category(), CurrencyValues.RUB,
                        40000.00, "SpendDescription_3", userJson.username()))
                .description());

        loginPage.login(userJson.username(), userJson.testData().password());

        mainPage
                .waitForPageLoaded()
                .checkSpendingsList(spends);
    }

    @Test
    @DbUser
    @DisplayName("Проверка удаления траты")
    void deletedSpendingTest(UserJson userJson) {

        List<String> spends = new ArrayList<>();

        CategoryJson category = spendApiClient.createCategory(new CategoryJson(
                null, "Обучение", userJson.username()));

        spends.add(spendApiClient.createSpend(new SpendJson(
                        null, new Date(), category.category(), CurrencyValues.RUB,
                        60000.00, "SpendDescription_1", userJson.username()))
                .description());

        spends.add(spendApiClient.createSpend(new SpendJson(
                        null, new Date(), category.category(), CurrencyValues.RUB,
                        90000.00, "SpendDescription_2", userJson.username()))
                .description());

        //не добавляю в список на проверку
        spendApiClient.createSpend(new SpendJson(
                null, new Date(), category.category(), CurrencyValues.RUB,
                40000.00, "SpendDescription_3", userJson.username()));

        loginPage.login(userJson.username(), userJson.testData().password());

        mainPage
                .selectSpendingByDescription("SpendDescription_3")
                .deleteSpending()
                .checkSpendingsList(spends);
    }

}