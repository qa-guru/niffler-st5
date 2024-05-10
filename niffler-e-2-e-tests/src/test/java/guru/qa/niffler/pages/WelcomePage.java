package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class WelcomePage {

    private final SelenideElement loginButton = $(byText("Login"));

    @Step("Нажать на кнопку Логин")
    public LoginPage clickLogin() {
        loginButton.click();
        return new LoginPage();
    }
}