package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.AuthPage;
import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.pages.StartPage;
import guru.qa.niffler.util.DbCreateUserExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(DbCreateUserExtension.class)
public class LoginTest extends BaseWebTest {

    @Test
    @TestUser
    void loginTest(UserJson userJson) {
        StartPage startPage = new StartPage();
        AuthPage authPage = new AuthPage();
        MainPage mainPage = new MainPage();

        startPage.openPage()
                .clickLoginButton();
        authPage.login(userJson.username(), userJson.testData().password());
        mainPage.openPage()
                .checkSpendingsSectionIsVisible();
    }
}