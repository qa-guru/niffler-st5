package guru.qa.niffler.test.user;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.BaseWebTest;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.jupiter.annotation.User.UserType.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebTest
public class UsersFriendsBeforeMethodTest extends BaseWebTest {

    @BeforeEach
    void doLogin(@User(INVITATION_SEND) UserJson sender, @User(WITH_FRIENDS) UserJson friend) {
        open(CFG.frontUrl());

        // Код, чтобы посмотреть, что пользователи были получены
        welcomePage.goToLogin();
        loginPage.login(
                sender.username(),
                sender.testData().password());
        mainPage.waitForPageLoaded();

        menu.getLogout().click();
        welcomePage.goToLogin();
        loginPage.login(
                friend.username(),
                friend.testData().password());
        mainPage.waitForPageLoaded();
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