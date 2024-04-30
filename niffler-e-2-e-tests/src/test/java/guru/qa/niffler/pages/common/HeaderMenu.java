package guru.qa.niffler.pages.common;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class HeaderMenu {

    private final SelenideElement friends = $("li[data-tooltip-id='friends']");
    private final SelenideElement people = $("li[data-tooltip-id='people']");
    private final SelenideElement avatar = $(".header__avatar");

    public boolean isPageLoaded() {
        return avatar.exists();
    }

    @Step
    public void openFriendsList() {
        friends.click();
    }

    @Step
    public void openPeopleList() {
        people.click();
    }
}