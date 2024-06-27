package guru.qa.niffler.ui.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage extends BasePage<ProfilePage> {

    public static final String URL = CONFIG.frontUrl() + "profile";

    private final SelenideElement firstnameInput = $("[name = 'firstname']");
    private final SelenideElement submitBtn = $("[type=submit]");

    @Step("Ввести имя {0}")
    public ProfilePage setName(String newName) {
        firstnameInput.clear();
        firstnameInput.sendKeys(newName);
        submitBtn.scrollTo().click();
        return this;
    }

    @Step("Проверить полe \"Имя\"")
    public void checkName(String expected) {
        firstnameInput.should(value(expected));
    }

}
