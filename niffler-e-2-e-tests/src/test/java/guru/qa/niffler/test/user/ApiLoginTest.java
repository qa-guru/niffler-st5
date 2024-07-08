package guru.qa.niffler.test.user;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith({
        BrowserExtension.class,
        ApiLoginExtension.class
})
public class ApiLoginTest {

    @ApiLogin(
            username = "dima",
            password = "12345"
    )
    @Test
    void updateProfileTest() {
        open(ProfilePage.URL, ProfilePage.class)
                .waitForPageLoaded();
    }
}
