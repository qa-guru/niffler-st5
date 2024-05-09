package guru.qa.niffler.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    // Selenide Elements
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement signUpButton = $(".form__submit");

    // Open the registration page
    public RegisterPage open() {
        Selenide.open("/register"); // Adjust the URL if needed
        return this;
    }

    // Set username
    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    // Set password
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    // Set submit password
    public RegisterPage setSubmitPassword(String password) {
        passwordSubmitInput.setValue(password);
        return this;
    }

    // Submit the registration form
    public void register() {
        signUpButton.click();
    }

    // Method to navigate to the login page if needed
    public void goToLogin() {
        $("a[href='http://127.0.0.1:3000/redirect']").click();
    }
}
