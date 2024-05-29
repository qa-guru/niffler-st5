package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.LoginPage;
import guru.qa.niffler.pages.WelcomePage;
import guru.qa.niffler.pages.common.HeaderMenu;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exist;
import static guru.qa.niffler.jupiter.annotation.User.UserType.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebTest
public class UsersFriendsBeforeMethodTest {

    private final WelcomePage welcomePage = new WelcomePage();
    private final LoginPage loginPage = new LoginPage();
    private final HeaderMenu menu = new HeaderMenu();

    @BeforeEach
    void doLogin(@User(INVITATION_SEND) UserJson sender, @User(WITH_FRIENDS) UserJson friend) {
        Selenide.open("http://127.0.0.1:3000");

        // Код, чтобы посмотреть, что пользователи были получены
        welcomePage.goToLogin();
        loginPage.login(
                sender.username(),
                sender.testData().password());
        menu.checkPageLoaded();

        menu.getLogout().click();
        welcomePage.goToLogin();
        loginPage.login(
                friend.username(),
                friend.testData().password());
        menu.checkPageLoaded();
    }

    //**************** INVITATION SEND ********************//

    @Description("Два пользователя одного типа с параметрами в тестовом методе." +
            " Два пользователя разного типа с параметрами в BeforeEach-методе")
    @Test
    void invitationSendTest(@User(INVITATION_RECEIVED) UserJson receiver_1,
                            @User(INVITATION_RECEIVED) UserJson receiver_2) {

        assertTrue(receiver_1.username().contains("_received"));
        assertTrue(receiver_2.username().contains("_received"));
        assertNotEquals(receiver_1.username(), receiver_2.username());
    }
}