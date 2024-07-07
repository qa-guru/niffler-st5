package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.AuthPage;
import guru.qa.niffler.pages.StartPage;
import guru.qa.niffler.util.DbCreateUserExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ExtendWith(DbCreateUserExtension.class)
public class LoginTest extends BaseWebTest{

    @Test
    @TestUser
    void loginTest(UserJson userJson) {
        StartPage startPage = new StartPage();
        AuthPage authPage = new AuthPage();

        Selenide.open(CFG.frontUrl() + "/login");
        startPage.clickLoginButton();
        authPage.login(userJson.username(), userJson.testData().password());
        Selenide.open(CFG.frontUrl() + "/main");
        $("a[href*='redirect']").should(visible);

    }
}