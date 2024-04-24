package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final ElementsCollection spendingRows = $(".spendings-table tbody").$$("tr");
    private final SelenideElement deleteSpendingBtn = $(".spendings__bulk-actions button");


    public SelenideElement findSpendingRowByDescription(String description) {
        return spendingRows.find(text(description));
    }

    public MainPage chooseSpending(SelenideElement spendingRow) {
        spendingRow.$$("td").first().scrollTo().click();
        return this;
    }

    public MainPage deleteSpending() {
        deleteSpendingBtn.click();
        return this;
    }

    public void checkCountOfSpendings(int expectedSize) {
        spendingRows.shouldHave(size(expectedSize));
    }

}
