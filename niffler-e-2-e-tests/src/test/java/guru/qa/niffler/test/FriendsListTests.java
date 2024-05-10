package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.pages.WelcomePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@WebTest
@ExtendWith(UserQueueExtension.class)
public class FriendsListTests {

    private final WelcomePage welcomePage = new WelcomePage();
    private final MainPage mainPage = new MainPage();


    @Test
    public void testInviteReceivedAllPeoplePage(@User(User.UserType.WITH_FRIENDS) UserJson user, @User(User.UserType.INVITATION_RECEIVED) UserJson newFriend) {
        Selenide.open("http://127.0.0.1:3000/");
        welcomePage.clickLogin()
                .login(user.username(), user.testData().password());
        mainPage.openAllPeopleList()
                .checkVisibleButtonsInviteReceived(newFriend.username());
    }

    @Test
    public void testAddFriendsAllPeoplePage(@User(User.UserType.INVITATION_SEND) UserJson user) {
        Selenide.open("http://127.0.0.1:3000/");
        welcomePage.clickLogin()
                .login(user.username(), user.testData().password());
        mainPage.openAllPeopleList().checkVisibleAddFriend("korova");
    }

    @Test
    public void testRemoveFriendAllPeoplePage(@User(User.UserType.WITH_FRIENDS) UserJson user) {
        Selenide.open("http://127.0.0.1:3000/");
        welcomePage.clickLogin()
                .login(user.username(), user.testData().password());
        mainPage.openAllPeopleList().
                checkVisibleButtonRemoveFriend("Bober");
    }

}
