package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage extends BasePage<ProfilePage> {

    public static final String URL = CFG.frontUrl() + "profile";

    private final SelenideElement avatar = $(".profile__avatar");

    @Step("")
    public ProfilePage setName() {
        // set name
        return this;
    }

    @Step("")
    public ProfilePage checkFields() {
        // check fields
        return this;
    }

    @Override
    public ProfilePage checkPageLoaded() {
        avatar.should(visible);
        return this;
    }
}
