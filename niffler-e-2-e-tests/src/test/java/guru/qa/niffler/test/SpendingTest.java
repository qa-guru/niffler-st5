package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.Spends;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;
import java.util.Objects;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.SpendsCondition.spendsInTable;


@Disabled
@WebTest
public class SpendingTest extends BaseWebTest {

    static {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin() {
        // createSpend
        Selenide.open(CFG.frontUrl());
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue("dima");
        $("input[name='password']").setValue("12345");
        $("button[type='submit']").click();
    }

    @Test
    void anotherTest() {
        Selenide.open(CFG.frontUrl());
        $("a[href*='redirect']").should(visible);
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
            category = "Обучение1",
            username = "dima"
    )
    @Spend(
            description = "QA.GURU Advanced 5",
            amount = 65000.00,
            currency = CurrencyValues.RUB
    )
    @Test
    void spendingShouldBeDeletedAfterTableAction(SpendJson spendJson) {
        SelenideElement rowWithSpending = $(".spendings-table tbody")
                .$$("tr")
                .find(text(spendJson.description()))
                .scrollIntoView(false);

        rowWithSpending.$$("td").first().click();
        $(".spendings__bulk-actions button").click();

        $(".spendings-table tbody").$$("tr")
                .shouldHave(size(0));
    }

    @Category(
            category = "Обучение3",
            username = "dima"
    )
    @Spends({
            @Spend(
                    description = "QA.GURU Advanced 5 - обучение",
                    amount = 65000.00,
                    currency = CurrencyValues.RUB
            ),
            @Spend(
                    description = "QA.GURU Advanced 5 - написание диплома",
                    amount = 1.00,
                    currency = CurrencyValues.RUB
            )
    })
    @Test
    void checkSpendingContent(SpendJson[] spends) {
        ElementsCollection spendings = $(".spendings-table tbody")
                .$$("tr");

        spendings.shouldHave(spendsInTable(spends));
    }
}
