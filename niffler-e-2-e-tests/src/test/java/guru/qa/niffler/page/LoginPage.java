package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement usernameInput = $(byName("username"));
    private final SelenideElement passwordInput = $(byName("password"));
    private final SelenideElement signInBtn = $("[type='submit']");

    public LoginPage setUsername(String username) {
        usernameInput.sendKeys(username);
        return this;
    }

    public LoginPage setPassword(String password) {
        passwordInput.sendKeys(password);
        return this;
    }

    public void clickSignIn() {
        signInBtn.click();
    }

}