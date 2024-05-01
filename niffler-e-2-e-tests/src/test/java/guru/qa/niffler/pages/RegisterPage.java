package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement formHeader = $(".form__header");
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement signInButton = $(".form__submit");

    public RegisterPage setUsername(String username) {

        usernameInput.setValue(username);
        return this;
    }

    public RegisterPage setPassword(String password) {

        passwordInput.setValue(password);
        return this;
    }
    public RegisterPage setPasswordSubmit(String password) {

        passwordSubmitInput.setValue(password);
        return this;
    }

    public void clickSignIn() {

        signInButton.click();
    }

    public RegisterPage register(String username, String password) {

        setUsername(username).setPassword(password).setPasswordSubmit(password).clickSignIn();

        return this;
    }
}
