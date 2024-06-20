package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class WelcomePage extends BasePage<WelcomePage> {

    public static final String URL = CFG.frontUrl();

    private final String welcomeMassage = "Welcome to magic journey with Niffler. The coin keeper";

    private final SelenideElement welcomeHeader = $(".main__header");
    private final SelenideElement loginBtn = $(byText("Login"));
    private final SelenideElement registerBtn = $(byText("Register"));

    @Step("Перейти на страницу входа в систему")
    public void goToLogin() {
        welcomeHeader.should(text(welcomeMassage));
        loginBtn.click();
        welcomeHeader.should(not(visible));
    }

    @Step("Перейти на страницу регистрации")
    public void goToRegister() {
        welcomeHeader.should(text(welcomeMassage));
        registerBtn.click();
        welcomeHeader.should(not(visible));
    }

    @Step("Check that page is loaded")
    @Override
    public WelcomePage waitForPageLoaded() {
        loginBtn.should(visible);
        return this;
    }
}
