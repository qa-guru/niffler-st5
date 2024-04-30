package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class HeaderPage {
    private final SelenideElement friendsNavigationButton = $("li[data-tooltip-content='Friends']");
    private final SelenideElement allPeopleNavigationButton = $("li[data-tooltip-content='All people']");
    private final SelenideElement logoutButton = $(".header__logout button");

    @Step("Open friends page")
    public FriendsPage openFriendsPage() {
        friendsNavigationButton.click();
        return new FriendsPage();
    }

    @Step("Open all people page")
    public AllPeoplePage openAllPeoplePage() {
        allPeopleNavigationButton.click();
        return new AllPeoplePage();
    }

    @Step("Click Logout button")
    public AuthorizationPage clickLogout() {
        logoutButton.click();
        return new AuthorizationPage();
    }
}