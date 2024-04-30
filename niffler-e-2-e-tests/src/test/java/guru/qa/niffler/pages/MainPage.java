package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final ElementsCollection spendings = $(".spendings-table tbody").$$("tr");
    private final SelenideElement progressBar = $("div[role='progressbar']");
    private final SelenideElement deleteAllSelectedBtn = $(".spendings__bulk-actions button");


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
}