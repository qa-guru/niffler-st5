package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.WelcomePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.jupiter.annotation.User.Selector.FRIEND;
import static guru.qa.niffler.jupiter.annotation.User.Selector.INVITE_RECEIVED;
import static guru.qa.niffler.jupiter.annotation.User.Selector.INVITE_SENT;

@WebTest
@ExtendWith({UsersQueueExtension.class, BrowserExtension.class})
public class FriendsTest {

    @Test
    void testCheckFriendIsPresent(@User(selector = FRIEND) UserJson userForTest) {

        open("http://127.0.0.1:3000/main", WelcomePage.class)
                .openLoginPage()
                .doLogin(userForTest.username(), userForTest.testData().password())
                .goToPeoplePage()
                .checkFriendIsPresent("DOG");
    }

    @Test
    void testCheckFriendIsInviteSent(@User(selector = INVITE_SENT) UserJson userForTest) {

        open("http://127.0.0.1:3000/main", WelcomePage.class)
                .openLoginPage()
                .doLogin(userForTest.username(), userForTest.testData().password())
                .goToPeoplePage()
                .addFriendIsInviteSent("DOG")
                .checkFriendIsInviteSent("DOG");
    }

    @Test
    void testCheckFriendInvitationReceived(@User(selector = INVITE_RECEIVED) UserJson userForTest) {
        open("http://127.0.0.1:3000/main", WelcomePage.class)
                .openLoginPage()
                .doLogin(userForTest.username(), userForTest.testData().password())
                .goToPeoplePage()
                .checkFriendIsInvitationReceived("CAT");
    }

    @Test
    void testCheckFriendsAndInvitationReceived(@User(selector = FRIEND) UserJson userForTest,
                                               @User(selector = INVITE_SENT) UserJson userForTest2) {

        open("http://127.0.0.1:3000/main", WelcomePage.class)
                .openLoginPage()
                .doLogin("DOG", "DOG")
                .goToPeoplePage()
                .checkFriendIsInvitationReceived(userForTest2.username())
                .checkFriendIsPresent(userForTest.username());
    }

}
