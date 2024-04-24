package guru.qa.niffler.pages.auth;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

public class LoginPage {
    private final SelenideElement usernameInput = Selenide.$("input[name='username']");
    private final SelenideElement passwordInput = Selenide.$("input[name='password']");
    private final SelenideElement submitBtn = Selenide.$("button[type='submit']");

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
