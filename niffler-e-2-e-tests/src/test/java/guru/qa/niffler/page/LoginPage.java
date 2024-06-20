package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage>{

    public static final String URL = CFG.authUrl() + "login";

    private final SelenideElement loginInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement signUp = $(byText("Sign up!"));
    private final SelenideElement loader = $(".loader");

    /**
     * Войти в систему
     */
    @Step("Войти в систему c учетными данными: логин: {0}, пароль: {1} ")
    public void login(String login, String password) {
        loginInput.setValue(login);
        passwordInput.setValue(password);
        submitButton.click();
        loader.should(not(visible), Duration.ofSeconds(10));
    }

    @Step("Перейти на страницу регистрации")
    public void goToRegister() {
        signUp.click();
    }

    @Step("Ожидание загрузки страницы")
    @Override
    public LoginPage waitForPageLoaded() {
        loginInput.should(visible);
        passwordInput.should(visible);
        return this;
    }
}