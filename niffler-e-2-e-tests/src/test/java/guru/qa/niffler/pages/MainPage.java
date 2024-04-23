package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverConditions.url;

public class MainPage extends BasePage<MainPage>{

    public final String url = "http://127.0.0.1:3000/main";
    private final SelenideElement spendingForm = $(".add-spending__form")
            .as("Форма создания трат");
    private final SelenideElement deleteSpendingBtn = $(".spendings__bulk-actions button")
            .as("Кнопка 'Удалить'");
    private final ElementsCollection rowsTable = $(".spendings-table tbody").$$("tr")
            .as("Строки таблицы");

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
    @Step("Удалить трату с текстом {0} в поле 'Description'")
    public MainPage deleteRowWithSpending(String description) {
        findRowWithSpending(description).$$("td").first()
                .scrollIntoView(true)
                .click();
        deleteSpendingBtn.click();
        return this;
    }
    @Step("Проверить, что трата с текстом {0} в поле 'Description' удалена")
    public MainPage checkRowWithSpendingIsExist(String description) {
        findRowWithSpending(description).shouldNot(visible);
        return this;
    }

    @Step("Найти трату с текстом {0} в поле 'Description'")
    private SelenideElement findRowWithSpending(String description) {
        return rowsTable.find(text(description));
    }
}
