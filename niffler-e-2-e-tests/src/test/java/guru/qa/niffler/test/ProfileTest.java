package guru.qa.niffler.test;

import com.github.javafaker.Faker;
import guru.qa.niffler.pages.ProfilePage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class ProfileTest extends BaseWebTest {
    @Test
    void updateProfileTest() {
        open(ProfilePage.URL, ProfilePage.class)
                .setName(Faker.instance().funnyName().name(), Faker.instance().funnyName().name())
                .checkToasterText("Profile successfully updated")
                .checkFields();
    }

}
