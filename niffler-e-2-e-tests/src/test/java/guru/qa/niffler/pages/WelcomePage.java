package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class WelcomePage {

    private final String welcomeMassage = "Welcome to magic journey with Niffler. The coin keeper";

    private final SelenideElement welcomeHeader = $(".main__header");
    private final SelenideElement loginBtn = $(byText("Login"));
    private final SelenideElement registerBtn = $(byText("Register"));

    /**
     * Перейти на страницу входа
     */
    public void goToLogin() {
        welcomeHeader.should(text(welcomeMassage));
        loginBtn.should(visible).click();
        welcomeHeader.should(not(visible));
    }

    /**
     * Перейти на страницу регистрации
     */
    public void goToRegister() {
        welcomeHeader.should(text(welcomeMassage));
        registerBtn.should(visible).click();
        welcomeHeader.should(not(visible));
    }

}
