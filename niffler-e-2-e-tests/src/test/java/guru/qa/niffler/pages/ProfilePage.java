package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage extends BasePage<ProfilePage> {

    public static final String URL = CFG.frontUrl() + "profile";

    private final SelenideElement avatar = $(".profile__avatar");

    public ProfilePage setName(String name, String surname) {
        $("[name='firstname']").setValue(name);
        $("[name='surname']").setValue(name);
        return this;
    }

    public ProfilePage checkFields() {
        return this;

    }

    @Override
    public ProfilePage checkPageLoaded() {
        avatar.should(visible);
        return this;
    }
}
