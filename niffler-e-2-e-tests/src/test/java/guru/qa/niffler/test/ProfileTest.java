package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.ui.page.StartPage;
import org.junit.jupiter.api.Test;

public class ProfileTest extends BaseWebTest {

    @Test
    @TestUser
    void updateProfileTest(UserJson user) {
        String name = new Faker().name().firstName();
        Selenide.open(StartPage.URL, StartPage.class)
                .login(user)
                .openProfile()
                .setName(name)
                .checkMessage("Profile successfully updated")
                .checkName(name);
    }
}
