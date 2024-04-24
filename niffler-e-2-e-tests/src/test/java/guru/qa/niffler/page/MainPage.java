package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;


import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;


public class MainPage {

    //Elements
    private final ElementsCollection sectionHistory = $(".spendings-table tbody").$$("tr");
    private final SelenideElement addSpendingSection = $(".main-content__section-add-spending"),
            categoryInput = addSpendingSection.$("div[id^='react-select-3-input']"),
            amountInput = addSpendingSection.$("input[name='amount']"),
            descriptionInput = addSpendingSection.$("input[name='description']"),
            calendarInput = addSpendingSection.$(".react-datepicker__input-container"),
            submitSpendingButton = addSpendingSection.$("button[type='submit']"),
            submitDeleteSelected = $(".spendings__bulk-actions button");

    //Actions
    public SelenideElement findSpendingRowByDescription(String description) {
        return sectionHistory.find(text(description));
    }
    public MainPage chooseSpending(SelenideElement sectionHistory) {
        sectionHistory.$$("td").first().scrollTo().click();
        return this;
    }

    public MainPage deleteSpending() {
        submitDeleteSelected.click();
        return this;
    }

    public void checkSpendingShouldBeDeletedAfterTableAction(int expectedSize) {
        sectionHistory.shouldHave(size(expectedSize));
    }

}
