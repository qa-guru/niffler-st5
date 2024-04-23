package guru.qa.niffler.pages.authentication;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class AuthorizationPage {
    private final SelenideElement logInButton = $x("//a[contains(text(), 'Login')]");
    private final SelenideElement usernameField = $("input[name='username']");
    private final SelenideElement passwordField = $("input[name='password']");
    private final SelenideElement sigInButton = $(".form__submit");

    @Step("Click LogIn")
    public AuthorizationPage clickLogInButton() {
        logInButton.click();
        return this;
    }

    @Step("Fill in Username field")
    public AuthorizationPage fillInUsernameField(String username) {
        usernameField.sendKeys(username);
        return this;
    }


    @Step("Fill in Password field")
    public AuthorizationPage fillInPasswordField(String password) {
        passwordField.sendKeys(password);
        return this;
    }

    @Step("Click SignIn")
    public void clickSignInButton() {
        sigInButton.click();
    }
}
