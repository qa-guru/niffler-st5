package guru.qa.niffler.test.spending;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepositoryJdbc;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.meta.JdbcTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.LoginPage;
import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.pages.WelcomePage;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static guru.qa.niffler.utils.DateHelper.convertStringToDate;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
public class SpendingJdbcTest {

    private final MainPage mainPage = new MainPage();
    private final WelcomePage welcomePage = new WelcomePage();
    private final LoginPage loginPage = new LoginPage();

    private final SpendRepositoryJdbc spendRepository = new SpendRepositoryJdbc();

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000");
        welcomePage.goToLogin();
        loginPage.login("dima", "12345");
    }

    @Category(
            category = "Обучение_Advanced 5",
            username = "dima")
    @Spend(
            spendDate = "2024-04-18",
            category = "Обучение_Advanced 5",
            currency = CurrencyValues.RUB,
            amount = 65000.00,
            description = "Description",
            username = "dima"
    )
    @DisplayName("Создание и редактирование Spend через запрос в БД ")
    @Description("Создаётся категория, затем расходник, в тесте проверяется что расходник отображается в списке." +
            "Между делом проверяю edit-методы, прямо в тесте, никак не упаковывая." +
            " После теста сначала удаляется расходник, потом удаляется категория.")
    @Test
    void spendingShouldBeDeletedAfterTableActionWithJdbcExt(SpendJson spendJson) {
        mainPage.selectSpendingByDescription(spendJson.description());

        assertTrue(mainPage.checkRow(spendJson.description()),
                "В таблице не найдена строка: " + spendJson.description());

        // change entities to test the edit methods
        CategoryEntity categoryEntity = new CategoryEntity();
        SpendEntity spendEntity = new SpendEntity();
        UUID catId = spendRepository.getCategoryId("Обучение_Advanced 5");
        UUID spendId = spendRepository.getSpendIdByCategoryId(catId);

        categoryEntity.setId(catId);
        categoryEntity.setUsername("dima");
        categoryEntity.setCategory("CHANGED");

        spendEntity.setId(spendId);
        spendEntity.setSpendDate(convertStringToDate("2022-02-11"));
        spendEntity.setCategory("CHANGED");
        spendEntity.setCurrency(CurrencyValues.RUB);
        spendEntity.setAmount(110000.00);
        spendEntity.setDescription("Обучение_CHANGED");
        spendEntity.setUsername("dima");

        spendRepository.editCategory(categoryEntity);
        spendRepository.editSpend(spendEntity);

        // refresh and check updates
        Selenide.refresh();

        assertTrue(mainPage.checkCategory("CHANGED"), "Category не найдено");
        assertTrue(mainPage.checkRow("Обучение_CHANGED"), "Description не найдено");
    }
}
