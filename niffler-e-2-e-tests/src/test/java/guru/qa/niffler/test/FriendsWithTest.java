package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.AllPeoplePage;
import guru.qa.niffler.pages.AuthorizationPage;
import guru.qa.niffler.pages.FriendsPage;
import guru.qa.niffler.pages.HeaderPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.User.Selector.*;

@WebTest
public class FriendsWithTest {
    private final AuthorizationPage authorizationPage = new AuthorizationPage();
    private final HeaderPage headerPage = new HeaderPage();
    private final FriendsPage friendsPage = new FriendsPage();
    private final AllPeoplePage allPeoplePage = new AllPeoplePage();

    static {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin(@User(selector = WITH_FRIENDS) UserJson userForTest) {
        Selenide.open("http://127.0.0.1:3000/");

        authorizationPage.clickLogInButton()
                .setUsername(userForTest.username())
                .setPassword(userForTest.testData().password())
                .clickSignInButton();
    }

    @Test
    void checkingYAFriendsStatusTest(@User(selector = ACCEPTED_FRIENDS) UserJson anotherUserForTest) {
        headerPage.openAllPeoplePage()
                .isStatusYAFriends(
                        allPeoplePage
                                .findFriendByUsername(anotherUserForTest.username()));

        headerPage.openFriendsPage()
                .isStatusYAFriends(
                        friendsPage
                                .findFriendByUsername(anotherUserForTest.username()));
    }

    @Test
    void checkingYAFriendsStatusTest1(@User(selector = ACCEPTED_FRIENDS) UserJson anotherUserForTest) {
        headerPage.openAllPeoplePage()
                .isStatusYAFriends(
                        allPeoplePage
                                .findFriendByUsername(anotherUserForTest.username()));

        headerPage.openFriendsPage()
                .isStatusYAFriends(
                        friendsPage
                                .findFriendByUsername(anotherUserForTest.username()));
    }

    @Test
    void checkingYAFriendsStatusTest2(@User(selector = ACCEPTED_FRIENDS) UserJson anotherUserForTest) {
        headerPage.openAllPeoplePage()
                .isStatusYAFriends(
                        allPeoplePage
                                .findFriendByUsername(anotherUserForTest.username()));

        headerPage.openFriendsPage()
                .isStatusYAFriends(
                        friendsPage
                                .findFriendByUsername(anotherUserForTest.username()));
    }
}
