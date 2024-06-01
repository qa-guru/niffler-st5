package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.SpendJson;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final ElementsCollection tableRows = $(".spendings-table").$(" tbody").$$("tr");
    private final SelenideElement deleteSpendingBtn = $(".spendings__table-controls").find(byText("Delete selected"));
    private final SelenideElement allPeople = $("[href='/people']");
    private SelenideElement spendingRow;

    public MainPage findSpendingByDescription(SpendJson spend) {
        spendingRow = tableRows.find(text(spend.description()));
        return this;
    }

    public MainPage chooseSpending() {
        spendingRow.$("td").scrollTo().click();
        return this;
    }

    public MainPage deleteChosenSpending() {
        deleteSpendingBtn.click();
        return this;
    }

    public PeoplePage clickAllPeople() {
        allPeople.click();
        return new PeoplePage();
    }

    public void assertThatTableContentHasSize(int expectedSize) {
        tableRows.shouldHave(size(expectedSize));
    }

}