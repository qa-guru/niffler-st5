package guru.qa.niffler.pages.main;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final SelenideElement table = $(".spendings-table tbody");

    private final SelenideElement deleteSelectedButton = $(".spendings__bulk-actions button");

    @Step("Find spending by description in table")
    public SelenideElement findSpendingByDescription(String description) {
        return table.$$("tr")
                .find(text(description));
    }

    @Step("Find spending by description in table")
    public MainPage choosingFirstSpending(SelenideElement firstRowWithSpending) {
        firstRowWithSpending.$$("td").first().scrollIntoView(true).click();
        return this;
    }

    @Step("Delete selected spending")
    public MainPage clickDeleteSelected() {
        deleteSelectedButton.click();
        return this;
    }

    @Step("Checking spending table size")
    public void checkingSpendingTableSize(int sizeShouldBe) {
        table.$$("tr")
                .shouldHave(size(sizeShouldBe));
    }
}
