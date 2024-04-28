package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class WelcomePage {
    public SelenideElement
            loginBtn = $("a[href*='redirect']"),
            registerBtn = $("a[href*='register']");

    public LoginPage clickLoginBtn() {
        loginBtn.click();
        return page(LoginPage.class);
    }

    public RegisterPage clickSignUpBtn() {
        registerBtn.click();
        return page(RegisterPage.class);
    }
}
