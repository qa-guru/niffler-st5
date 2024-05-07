package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final ElementsCollection table = $(".spendings-table tbody").$$("tr");

    private final SelenideElement deleteSelectedButton = $(".spendings__bulk-actions button");

    @Step("Find spending by description in table")
    public SelenideElement findSpendingByDescription(String description) {
        return table.find(text(description));
    }

    @Step("Find spending by description in table")
    public MainPage choosingFirstSpending(SelenideElement rowWithSpending) {
        rowWithSpending.$$("td").first().scrollIntoView(true).click();
        return this;
    }

    @Step("Delete selected spending")
    public MainPage clickDeleteSelected() {
        deleteSelectedButton.click();
        return this;
    }

    @Step("Verify expected table size")
    public void expectedTableSize(int sizeShouldBe) {
        table.shouldHave(size(sizeShouldBe));
    }
}
