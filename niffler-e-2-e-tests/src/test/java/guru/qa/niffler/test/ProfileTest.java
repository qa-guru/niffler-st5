package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.page.message.SuccessMsg;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@Disabled
public class ProfileTest extends BaseWebTest {

    @Test
    void updateProfileTest() {
        open(ProfilePage.URL, ProfilePage.class)
                .setName("")
                .checkToasterMessage("Profile successfully updated")
                .checkName("")
                .checkToasterMessage("Profile successfully updated");
    }
}
