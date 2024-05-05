package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.selector.ByText;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.format;

public class MainPage {

    private final ElementsCollection spendings = $(".spendings-table tbody").$$("tr");
    private final SelenideElement progressBar = $("div[role='progressbar']");
    private final SelenideElement deleteAllSelectedBtn = $(".spendings__bulk-actions button");
    private final SelenideElement categoryDropdown =
            $x("//div[contains(text(), 'Choose spending category')]/parent::div/following-sibling::div");

    /**
     * Выбрать строку расходов по описанию
     */
    public MainPage selectSpendingByDescription(String description) {
        SelenideElement rowWithSpending = spendings.find(text(description));
        rowWithSpending.$("td").scrollIntoView(true).click();
        return this;
    }

    /**
     * Удалить выбранные расходы
     */
    public void deleteSpending() {
        deleteAllSelectedBtn.click();
        progressBar.should(not(visible), Duration.ofSeconds(10));
    }

    /**
     * Проверка видимости строки в таблице расходов по description расхода
     */
    public boolean checkRow(String description) {
        return spendings.find(text(description)).exists();
    }

    public boolean checkCategory(String value) {
        try {
            categoryDropdown.click();
            $(new ByText(value)).click();
            return true;
        } catch (ElementNotFound exception) {
            throw new RuntimeException("Category not found");
        }
    }
}