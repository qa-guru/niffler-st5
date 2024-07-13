package guru.qa.niffler.ui.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.ui.component.ReactCalendar;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.condition.spend.SpendsCondition.spendsInTable;

public class MainPage extends BasePage<MainPage> {

    public static final String URL = CONFIG.frontUrl() + "/main";

    private final ReactCalendar reactCalendar = new ReactCalendar();
    private final ElementsCollection tableRows = $(".spendings-table").$(" tbody").$$("tr");
    private final SelenideElement deleteSpendingBtn = $(".spendings__table-controls").find(byText("Delete selected"));
    private final SelenideElement allPeople = $("[href='/people']");
    private final SelenideElement profile = $("[href='/profile']");
    private final SelenideElement categorySelector = $(".select-wrapper");
    private final ElementsCollection dropdownOptions = $$("[id*='react-select']");
    private final SelenideElement amountInput = $("[type='number']");
    private final SelenideElement descriptionInput = $("[name='description']");
    private final SelenideElement addSpendingBtn = $("[type='submit']");

    @Step("Проверить отображение всех трат")
    public void checkSpends(SpendJson[] spendJsons) {
        tableRows.shouldHave(spendsInTable(spendJsons));
    }

    @Step("Выбрать категорию")
    public MainPage chooseCategory(String category) {
        categorySelector.click();
        dropdownOptions.find(Condition.text(category)).click();
        return this;
    }

    @Step("Ввести сумму")
    public MainPage setAmount(double amount) {
        amountInput.sendKeys(String.valueOf(amount));
        return this;
    }

    @Step("Выбрать описание")
    public MainPage setDescription(String description) {
        descriptionInput.sendKeys(description);
        return this;
    }

    @Step("Нажать \"ADD NEW SPENDING\"")
    public MainPage createSpend() {
        addSpendingBtn.click();
        return this;
    }

    @Step("Проверить, что список трат содержит трату {spendJson.description}")
    public void assertThatHistoryContainsSpend(SpendJson spendJson) {
        Assertions.assertFalse(tableRows.isEmpty());
        SelenideElement row = tableRows.find(Condition.text(spendJson.description()));
        row.shouldHave(Condition.text(String.valueOf(spendJson.amount())));
        row.shouldHave(Condition.text(spendJson.currency().name()));
        row.shouldHave(Condition.text(spendJson.category()));
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy", Locale.ENGLISH);
        row.shouldHave(Condition.text(formatter.format(spendJson.spendDate())));
    }

    @Step("Открыть профиль пользователя")
    public ProfilePage openProfile() {
        profile.click();
        return new ProfilePage();
    }

    @Step("Установить дату")
    public MainPage setDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(date);
        reactCalendar.set(formattedDate);
        return this;
    }

    @Step("Выбрать трату {spend.description}")
    public MainPage chooseSpending(SpendJson spend) {
        SelenideElement spendingRow = tableRows.find(text(spend.description()));
        spendingRow.$("td").scrollTo().click();
        return this;
    }

    @Step("Удалить выбранную трату")
    public MainPage deleteChosenSpending() {
        deleteSpendingBtn.click();
        return this;
    }

    @Step("Нажать \"ALL PEOPLE\"")
    public PeoplePage clickAllPeople() {
        allPeople.click();
        return new PeoplePage();
    }

    @Step("Проверить, что таблица содержит {0} трат")
    public void assertThatTableContentHasSize(int expectedSize) {
        tableRows.shouldHave(size(expectedSize));
    }

}