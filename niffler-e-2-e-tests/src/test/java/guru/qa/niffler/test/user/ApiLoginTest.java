package guru.qa.niffler.test.user;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.DbUser;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.DbCreateUserExtension;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith({
        BrowserExtension.class,
        DbCreateUserExtension.class,
        ApiLoginExtension.class
})
public class ApiLoginTest extends BaseWebTest {

    @ApiLogin(username = "dima", password = "12345")
    @Test
    void updateProfileTest() {
        open(ProfilePage.URL, ProfilePage.class)
                .waitForPageLoaded();
    }

    @ApiLogin(user = @DbUser)
    @Test
    void updateProfileTest2() {
        open(ProfilePage.URL, ProfilePage.class)
                .waitForPageLoaded();
    }
}
