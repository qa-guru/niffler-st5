package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.UserJson;

import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement usernameInput = $(byName("username"));
    private final SelenideElement passwordInput = $(byName("password"));
    private final SelenideElement signInBtn = $("[type='submit']");

    public LoginPage setUsername(String username) {
        usernameInput.sendKeys(username);
        return this;
    }

    public LoginPage setPassword(String password) {
        passwordInput.sendKeys(password);
        return this;
    }

    public MainPage clickSignIn() {
        signInBtn.click();
        return new MainPage();
    }

    public MainPage login(UserJson user) {
        setUsername(user.username());
        setPassword(user.testData().password());
        clickSignIn();
        return new MainPage();
    }

}