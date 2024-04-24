package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    //Elements
    private final SelenideElement usernameInput = $("input[name='username']"),
            passwordInput = $("input[name='password']"),
            submitButton = $("button[type='submit']"),
            errorForm = $(".form__error");

    //Action
    public LoginPage fillLoginPass(String login, String password) {
        setUsername(login);
        setPassword(password);
        return this;
    }

    public LoginPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public LoginPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public LoginPage clickSubmit() {
        submitButton.click();
        return this;
    }

    public LoginPage errorForm() {
        errorForm.shouldHave(text("Bad credentials"));
        return this;
    }

}
