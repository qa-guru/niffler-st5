package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage extends BasePage<ProfilePage> {

    public static final String URL = CFG.frontUrl() + "profile";

    private final SelenideElement userName = $(".avatar-container figcaption");
    private final SelenideElement nameInput = $("input[name='firstname']");

    @Step("Set name: {0}")
    public ProfilePage setName(String name) {
        nameInput.setValue(name);
        return this;
    }

    @Step("Check that page is loaded")
    @Override
    public ProfilePage waitForPageLoaded() {
        userName.should(visible);
        return this;
    }

    @Step("Check name: {0}")
    public ProfilePage checkName(String name) {
        nameInput.shouldHave(value(name));
        return this;
    }
}
