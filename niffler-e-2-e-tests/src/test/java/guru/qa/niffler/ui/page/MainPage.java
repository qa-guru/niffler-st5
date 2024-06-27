package guru.qa.niffler.ui.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.ui.component.ReactCalendar;
import io.qameta.allure.Step;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BasePage<MainPage> {

    public static final String URL = CONFIG.frontUrl() + "/main";

    private final ReactCalendar reactCalendar = new ReactCalendar();
    private final ElementsCollection tableRows = $(".spendings-table").$(" tbody").$$("tr");
    private final SelenideElement deleteSpendingBtn = $(".spendings__table-controls").find(byText("Delete selected"));
    private final SelenideElement allPeople = $("[href='/people']");
    private final SelenideElement profile = $("[href='/profile']");

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