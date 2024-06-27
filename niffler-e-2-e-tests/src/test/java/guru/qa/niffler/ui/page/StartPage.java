package guru.qa.niffler.ui.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class StartPage extends BasePage<StartPage> {

    public static final String URL = CONFIG.frontUrl() + "main";

    private final SelenideElement loginBtn = $(byText("Login"));

    @Step("Нажать Login")
    public LoginPage clickLogin() {
        loginBtn.click();
        return new LoginPage();
    }

    @Step("Перейти на главную страницу под пользователем {user.username}")
    public MainPage login(UserJson user) {
        clickLogin().login(user);
        return new MainPage();
    }

}