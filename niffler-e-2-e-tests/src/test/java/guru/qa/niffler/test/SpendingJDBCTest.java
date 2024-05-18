package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.Repository;
import guru.qa.niffler.data.repository.RepositoryJdbc;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.meta.WebTestJDBC;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.LoginPage;
import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.pages.WelcomePage;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;
import java.util.Objects;


@WebTestJDBC
public class SpendingJDBCTest {
    public final Repository repository = new RepositoryJdbc();

    static {
        Configuration.browserSize = "1920x1080";
    }

    private final WelcomePage welcomePage = new WelcomePage();
    private final LoginPage loginPage = new LoginPage();
    private final MainPage mainPage = new MainPage();

    @BeforeEach
    void doLogin() {
        // createSpend
        UserJson dima = UserJson.simpleUser("dima1", "cat1");
        Selenide.open("http://127.0.0.1:3000/");
        welcomePage.loginBtnClick();
        loginPage.fillLoginPage(dima);
    }

    @AfterEach
    void doScreenshot() {
        Allure.addAttachment(
                "Screen on test end",
                new ByteArrayInputStream(
                        Objects.requireNonNull(
                                Selenide.screenshot(OutputType.BYTES)
                        )
                )
        );
    }

    @Category(
            category = "Обучение3599",
            username = "dima1"
    )
    @Spend(
            description = "QA.GURU Advanced 5",
            amount = 65000.00,
            currency = CurrencyValues.RUB,
            category = @Category(
                    category = "Обучение3599",
                    username = "dima1"
            ),
            username = "dima1")

    @Test
    void spendingShouldBeDeletedAfterTableAction(CategoryJson categoryJson, SpendJson spendJson) {
        mainPage.spendingSizeShouldBe(1);

        String newCategoryName = "new" + categoryJson.category();
        CategoryEntity tempCategoryEntity = CategoryEntity.simpleCategoryEntity(categoryJson.id(), newCategoryName, categoryJson.username());
        repository.editCategory(tempCategoryEntity);

        Selenide.refresh();
        mainPage.assertSpendOnPage(0, spendJson.spendDate(), spendJson.amount(), spendJson.currency(), newCategoryName, spendJson.description());

        SpendEntity tempSpendEntity = new SpendEntity();
        tempSpendEntity.setId(spendJson.id());
        tempSpendEntity.setCurrency(spendJson.currency());
        tempSpendEntity.setUsername(spendJson.username());
        tempSpendEntity.setSpendDate(spendJson.spendDate());
        tempSpendEntity.setAmount(spendJson.amount() + 5d);
        tempSpendEntity.setDescription("test" + spendJson.description());
        tempSpendEntity.setCategory(tempCategoryEntity);

        repository.editSpend(tempSpendEntity);
        Selenide.refresh();
        mainPage.spendingSizeShouldBe(1)
                .assertSpendOnPage(0, tempSpendEntity.getSpendDate(), tempSpendEntity.getAmount(), tempSpendEntity.getCurrency(), newCategoryName, tempSpendEntity.getDescription());
    }
}
