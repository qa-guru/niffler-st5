package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
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
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;
import java.util.Objects;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;


@WebTestJDBC
public class SpendingJDBCTest {
    public final Repository repository = new RepositoryJdbc();

    static {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin() {
        // createSpend
        Selenide.open("http://127.0.0.1:3000/");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue("dima1");
        $("input[name='password']").setValue("cat1");
        $("button[type='submit']").click();
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
        SelenideElement rowWithSpending = $(".spendings-table tbody")
                .$$("tr")
                .find(text(spendJson.description()))
                .scrollIntoView(false);

        //rowWithSpending.$$("td").first().scrollTo().click();
        //$(".spendings__bulk-actions button").click();

        $(".spendings-table tbody").$$("tr")
                .shouldHave(size(1));

        CategoryEntity tempCategoryEntity = new CategoryEntity();
        tempCategoryEntity.setId(categoryJson.id());
        tempCategoryEntity.setCategory("new" + categoryJson.category());
        tempCategoryEntity.setUsername(categoryJson.username());

        repository.editCategory(tempCategoryEntity);
        Selenide.refresh();
        //проверка что что-то изменилось

        SpendEntity tempSpendEntity = new SpendEntity();
        tempSpendEntity.setId(spendJson.id());
        tempSpendEntity.setCurrency(spendJson.currency());
        tempSpendEntity.setUsername(spendJson.username());
        tempSpendEntity.setSpendDate(spendJson.spendDate());
        tempSpendEntity.setAmount(spendJson.amount() + 5d);
        tempSpendEntity.setDescription("test" + spendJson.description());
        tempSpendEntity.setCategory(tempCategoryEntity);

        repository.editSpend(tempSpendEntity);
        //проверка что что-то изменилось
    }
}
