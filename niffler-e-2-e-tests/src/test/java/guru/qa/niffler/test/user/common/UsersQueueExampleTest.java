package guru.qa.niffler.test.user.common;

import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@WebTest
@ExtendWith(UserQueueExtension.class)
public class UsersQueueExampleTest extends BaseWebTest {

    @Test
    void loginTest0(UserJson testUser) {
        open(CFG.frontUrl());
        welcomePage.goToLogin();
        loginPage.login(testUser.username(), testUser.testData().password());
        mainPage.waitForPageLoaded();
    }

    @Test
    void loginTest1(UserJson testUser) {
        open(CFG.frontUrl());
        welcomePage.goToLogin();
        loginPage.login(testUser.username(), testUser.testData().password());
        mainPage.waitForPageLoaded();
    }

    @Test
    void loginTest2(UserJson testUser) {
        open(CFG.frontUrl());
        welcomePage.goToLogin();
        loginPage.login(testUser.username(), testUser.testData().password());
        mainPage.waitForPageLoaded();
    }

    @Test
    void loginTest3(UserJson testUser) {
        open(CFG.frontUrl());
        welcomePage.goToLogin();
        loginPage.login(testUser.username(), testUser.testData().password());
        mainPage.waitForPageLoaded();
    }

    @Test
    void loginTest4(UserJson testUser) {
        open(CFG.frontUrl());
        welcomePage.goToLogin();
        loginPage.login(testUser.username(), testUser.testData().password());
        mainPage.waitForPageLoaded();
    }
}
