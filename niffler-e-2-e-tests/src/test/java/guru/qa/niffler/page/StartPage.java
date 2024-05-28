package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.UserJson;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class StartPage {

    private final SelenideElement loginBtn = $(byText("Login"));

    public LoginPage clickLogin() {
        loginBtn.click();
        return new LoginPage();
    }

    public MainPage login(UserJson user) {
        clickLogin().login(user);
        return new MainPage();
    }

}