package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;


public class MainPage extends BasePage<MainPage> {

    private final ElementsCollection spendingRows = $(".spendings-table tbody")
            .$$("tr");

    private final SelenideElement deleteButton = $(".spendings__bulk-actions button");

    private final SelenideElement friendsButton = $("a[href*='friends']");
    private final SelenideElement peopleButton = $("a[href*='people']");
    private final SelenideElement logOutButton = $(".header__logout");
    private final ReactCalendar calendar = new ReactCalendar();
    public MainPage openPage() {

        Selenide.open("http://127.0.0.1:3000/main");
        return this;
    }

    public MainPage clickCheckbox(String description) {

        spendingRows.findBy(text(description))
                .$$("td").first().scrollIntoView(true)
                .$("input[type='checkbox']").click();
        return this;
    }

    public void clickFriendsButton() {

        friendsButton.click();
    }

    public void clickPeopleButton() {

        peopleButton.click();
    }

    public MainPage deleteSpending() {

        deleteButton.click();
        return this;
    }

    public MainPage checkSpendingsCount(int count) {

        spendingRows.shouldHave(size(count));
        return this;
    }

    public MainPage checkSpendingsDeletedText() {

        $(".Toastify__toast-body").shouldHave(text("Spendings deleted"));
        return this;
    }

    public void logOut() {
        logOutButton.click();
    }

    @Override
    public MainPage checkPageLoaded() {
        return null;
    }
}

