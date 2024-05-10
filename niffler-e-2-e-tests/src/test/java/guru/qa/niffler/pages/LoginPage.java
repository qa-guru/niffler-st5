package guru.qa.niffler.pages;


import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement usernameField = $("input[name='username']");
    private final SelenideElement passwordField = $("input[name='password']");
    private final SelenideElement sigInButton = $(".form__submit");

    @Step("Ввести логин в поле имя")
    public LoginPage setUsername(String name) {
        usernameField.setValue(name);
        return this;
    }

    @Step("Ввести пароль в поле пароль")
    public LoginPage setPassword(String password) {
        passwordField.setValue(password);
        return this;
    }

    @Step("Кликнуть по кнопке войти в систему")
    public void clickSignIn() {
        sigInButton.click();
    }


    @Step("Залогиниться")
    public void login(String username, String password) {
        setUsername(username);
        setPassword(password);
        clickSignIn();
    }
}