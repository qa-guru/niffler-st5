package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement
            usernameInput = $("input[name='username']"),
            passwordInput = $("input[name='password']"),
            loginBtn = $("button[type='submit']"),
            signUpLink = $("a[href='/register']"),
            loader = $(".loader");

    public LoginPage doLogin(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        loginBtn.click();
        loader.shouldBe(visible).shouldNotBe(visible);
        return this;
    }

    public void clickSignUpLink() {
        signUpLink.click();
    }


}
