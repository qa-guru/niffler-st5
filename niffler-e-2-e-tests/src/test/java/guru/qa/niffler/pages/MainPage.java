package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


public class MainPage {

    private final ElementsCollection rowsTableSpending = $(".spendings-table tbody").$$("tr");
    private final SelenideElement deletedButton = $(".spendings__bulk-actions button");
    private final SelenideElement iconFriends = $("a[href='/friends']");
    private final SelenideElement iconAllPeople = $(".header [data-tooltip-id='people']");

    @Step("Выбор строки в таблице по описанию")
    public MainPage choosingRowByDescription(String description) {
        SelenideElement rowWithSpending = rowsTableSpending.find(text(description));
        rowWithSpending.$("td").scrollIntoView(true).click();
        return this;
    }

    @Step("Кликнуть кнопку удалить")
    public MainPage deleteSpending() {

        deletedButton.shouldBe(enabled, Duration.ofSeconds(5)).click();
        return this;
    }

    @Step("Проверка размера таблицы покупок")
    public void checkSizeSpendingTable(int size) {
        rowsTableSpending.shouldHave(size(size));
    }


    @Step("Нажать на иконку Все люди")
    public AllPeopleListPage openAllPeopleList() {
        iconAllPeople.click();
        return new AllPeopleListPage();
    }
}