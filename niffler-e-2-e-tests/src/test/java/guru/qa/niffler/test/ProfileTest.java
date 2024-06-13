package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class ProfileTest extends BaseWebTest {

    @ApiLogin(
            username = "dima",
            password = "12345"
    )
    @Test
    void updateProfileTest() {
        open(ProfilePage.URL, ProfilePage.class)
                .setName("")
                .checkToasterMessage("Profile successfully updated")
                .checkName("")
                .checkToasterMessage("Profile successfully updated");
    }
}
