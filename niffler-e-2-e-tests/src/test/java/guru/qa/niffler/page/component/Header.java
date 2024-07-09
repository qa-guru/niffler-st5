package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.ProfilePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {

    public Header() {
        super($(".header"));
    }

    private final SelenideElement avatar = $(".header__avatar");
    private final SelenideElement friendsButton = self.$("a[href*='/friends']");
    private final SelenideElement profileButton = self.$(".header__avatar");

    @Step("Open Friends page")
    public FriendsPage toFriendsPage() {
        friendsButton.click();
        return new FriendsPage();
    }

    @Step("Open Profile page")
    public ProfilePage toProfilePage() {
        profileButton.click();
        return new ProfilePage();
    }
}
