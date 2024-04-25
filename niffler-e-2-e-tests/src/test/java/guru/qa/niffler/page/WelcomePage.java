package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class WelcomePage {
    public SelenideElement
            loginBtn = $("a[href*='redirect']"),
            registerBtn = $("a[href*='register']");

    public void clickLoginBtn() {
        loginBtn.click();
    }

    public void clickSignUpBtn() {
        registerBtn.click();
    }
}
