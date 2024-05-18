package guru.qa.niffler.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.UserJson;

public class LoginPage {
    private final SelenideElement usernameInput = Selenide.$("input[name='username']");
    private final SelenideElement passwordInput = Selenide.$("input[name='password']");
    private final SelenideElement submitBtn = Selenide.$("button[type='submit']");

    public LoginPage fillLoginPage(UserJson userJson) {
        setUsername(userJson.username());
        setPassword(userJson.testData().password());
        clickSubmitBtn();
        return this;
    }

    public LoginPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public LoginPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public void clickSubmitBtn() {
        submitBtn.click();
    }
}