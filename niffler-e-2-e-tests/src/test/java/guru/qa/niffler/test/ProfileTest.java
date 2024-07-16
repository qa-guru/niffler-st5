package guru.qa.niffler.test;

import guru.qa.niffler.page.ProfilePage;
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
