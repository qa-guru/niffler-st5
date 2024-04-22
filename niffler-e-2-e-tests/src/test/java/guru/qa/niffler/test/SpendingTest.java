package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.extension.SpendExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

@ExtendWith({SpendExtension.class})
public class SpendingTest {

    static {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/");
        $("a[href*='/redirect']").click();
        $("input[name='username']").setValue("Aleksei");
        $("input[name='password']").setValue("Pass123");
        $("button[type='submit']").click();
    }

    @Spend(
            username = "Aleksei",
            description = "QA GURU ADVANCED 5",
            amount = 75000.00,
            currency = CurrencyValues.RUB,
            category = "Обучение"
    )
    @Test
    void spendingShouldBeDeletedAfterTableAction(SpendJson spendJson) {

        SelenideElement rowWithSpending = $(".spendings-table tbody")
                .$$("tr")
                .findBy(text(spendJson.description()));

        rowWithSpending.$$("td").first().scrollTo().click();
        $(".spendings__bulk-actions button").click();
        $(".spendings__bulk-actions tbody").$$("tr").shouldHave(size(0));
    }
}
