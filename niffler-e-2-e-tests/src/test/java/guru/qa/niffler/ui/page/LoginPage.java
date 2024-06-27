package guru.qa.niffler.ui.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {

    public static final String URL = CONFIG.frontUrl() + "/login";

    private final SelenideElement usernameInput = $(byName("username"));
    private final SelenideElement passwordInput = $(byName("password"));
    private final SelenideElement signInBtn = $("[type='submit']");

    @Step("Ввести имя пользователя \"{0}\"")
    public LoginPage setUsername(String username) {
        usernameInput.sendKeys(username);
        return this;
    }

    @Step("Ввести пароль")
    public LoginPage setPassword(String password) {
        passwordInput.sendKeys(password);
        return this;
    }

    @Step("Нажать Login")
    public MainPage clickSignIn() {
        signInBtn.click();
        return new MainPage();
    }

    @Step("Выполнить логин пользователя {user.username}")
    public MainPage login(UserJson user) {
        setUsername(user.username());
        setPassword(user.testData().password());
        clickSignIn();
        return new MainPage();
    }

}