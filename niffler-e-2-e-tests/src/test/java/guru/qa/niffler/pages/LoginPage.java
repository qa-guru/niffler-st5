package guru.qa.niffler.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.WebDriverConditions.url;

public class LoginPage extends BasePage<LoginPage>{

    public final String url = "http://127.0.0.1:9000/login";
    private final SelenideElement userNameField = $("input[name='username']").as("Логин");
    private final SelenideElement passwordField = $("input[name='password']").as("Пароль");
    private final SelenideElement submitBtn = $("button[type='submit']").as("Кнопка 'Войти'");

    @Override
    public LoginPage waitForPageLoaded() {
        userNameField.shouldBe(visible);
        return null;
    }

    @Override
    public LoginPage checkPage() {
        Selenide.webdriver()
                .shouldHave(url(url));
        return this;
    }

    @Step("Авторизоваться пользователем с именем {0}")
    public MainPage doLogin(String username, String password) {
        userNameField.setValue(username);
        passwordField.setValue(password);
        submitBtn.click();
        return page(MainPage.class);
    }
}
