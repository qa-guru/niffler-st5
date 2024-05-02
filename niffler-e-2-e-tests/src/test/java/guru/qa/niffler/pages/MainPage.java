package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverConditions.url;

public class MainPage extends BasePage<MainPage> {

    public final String url = "http://127.0.0.1:3000/main";
    private final SelenideElement spendingForm = $(".add-spending__form")
            .as("Форма создания трат");
    private final SelenideElement deleteSpendingBtn = $(".spendings__bulk-actions button")
            .as("Кнопка 'Удалить'");
    private final ElementsCollection rowsSpendingTable = $(".spendings-table tbody").$$("tr")
            .as("Строки таблицы");
    private final SelenideElement peopleLink = $("[href='/people']")
            .as("Кнопка перехода на странице People");

    @Override
    public MainPage waitForPageLoaded() {
        spendingForm.shouldBe(visible);
        return this;
    }

    @Override
    public MainPage checkPage() {
        Selenide.webdriver()
                .shouldHave(url(url));
        return this;
    }

    @Step("Выбрать трату с текстом {0} в поле 'Description'")
    public MainPage chooseSpending(SelenideElement rowWithSpending) {
        rowWithSpending.$$("td").first()
                .scrollIntoView(true)
                .click();
        return this;
    }

    @Step("Удалить трату")
    public MainPage deleteSpending() {
        deleteSpendingBtn.click();
        return this;
    }

    @Step("Проверить, что в таблице {0} записей  с тратами")
    public void checkCountOfSpendings(int expectedSize) {
        rowsSpendingTable.shouldHave(size(expectedSize));
    }

    @Step("Найти трату с текстом {0} в поле 'Description'")
    public SelenideElement findRowWithSpendingByDescription(String description) {
        return rowsSpendingTable.find(text(description));
    }

    @Step("Перейти на страницу People")
    public PeoplePage goToPeoplePage() {
        peopleLink.shouldBe(visible).click();
        return new PeoplePage();
    }
}
