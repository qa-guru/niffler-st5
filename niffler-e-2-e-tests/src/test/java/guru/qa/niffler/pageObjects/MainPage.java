package guru.qa.niffler.pageObjects;

import com.codeborne.selenide.SelenideElement;
import com.mifmif.common.regex.Main;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Step;
import lombok.SneakyThrows;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.test.SpendingTest.CENTER;

public class MainPage extends BasePage<MainPage> {

    public MainPage selectCategory(SpendJson spendJson) {
        SelenideElement rowWithSpending = $(".spendings-table tbody")
                .$$("tr")
                .find(text(spendJson.description()));
        rowWithSpending.$$("td")
                .first()
                .scrollIntoView(CENTER)
                .click();
        return this;
    }

    public MainPage deleteCategory() {
        $(".spendings__bulk-actions button").click();
        $(".spendings-table tbody").$$("tr")
                .shouldHave(size(0));
        return this;
    }

}
