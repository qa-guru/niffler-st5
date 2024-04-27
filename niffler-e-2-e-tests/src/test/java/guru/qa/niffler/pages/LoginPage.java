package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement loginField = $("input[name='username']");
    private final SelenideElement passwordField = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement signUp = $(byText("Sign up!"));
    private final SelenideElement loader = $(".loader");

    /**
     * Войти в систему
     */
    public void login(String login, String password) {
        loginField.should(editable).setValue(login);
        passwordField.should(editable).setValue(password);
        submitButton.click();
        loader.should(not(visible), Duration.ofSeconds(10));
    }

    /**
     * Перейти на страницу регистрации
     */
    public void goToRegister() {
        signUp.should(visible).click();
    }
}
