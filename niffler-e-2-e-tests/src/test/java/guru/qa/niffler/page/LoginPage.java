package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement loginInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement signUp = $(byText("Sign up!"));
    private final SelenideElement loader = $(".loader");

    /**
     * Войти в систему
     */
    @Step
    public void login(String login, String password) {
        loginInput.setValue(login);
        passwordInput.setValue(password);
        submitButton.click();
        loader.should(not(visible), Duration.ofSeconds(10));
    }

    /**
     * Перейти на страницу регистрации
     */
    public void goToRegister() {
        signUp.click();
    }
}