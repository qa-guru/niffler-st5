package guru.qa.niffler.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement signInButton = $(".form__submit");

    public void checkIsError() {
        $(".form__error").shouldBe(Condition.visible);
    }

    // Open the login page
    public LoginPage open() {
        Selenide.open("/login"); // Adjust the URL if needed
        return this;
    }

    // Set username
    public LoginPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    // Set password
    public LoginPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    // Submit the form
    public void signIn() {
        signInButton.click();
    }

    // Additional functionality to interact with the password visibility toggle
    public LoginPage togglePasswordVisibility() {
        $(".form__password-button").click();
        return this;
    }

    // Method to navigate to the registration page if needed
    public void goToRegistration() {
        $("a[href='/register']").click();
    }
}
