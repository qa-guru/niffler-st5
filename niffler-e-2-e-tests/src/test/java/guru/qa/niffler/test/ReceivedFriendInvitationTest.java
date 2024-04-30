package guru.qa.niffler.test;

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

import static guru.qa.niffler.jupiter.annotation.User.Selector.INVITATION_RECEIVED;
import static guru.qa.niffler.jupiter.annotation.User.Selector.INVITATION_SENT;

@WebTest
public class ReceivedFriendInvitationTest {
    private final AuthorizationPage authorizationPage = new AuthorizationPage();
    private final HeaderPage headerPage = new HeaderPage();
    private final FriendsPage friendsPage = new FriendsPage();
    private final AllPeoplePage allPeoplePage = new AllPeoplePage();

    @BeforeEach
    void doLogin(@User(selector = INVITATION_RECEIVED) UserJson userForTest) {
        Selenide.open("http://127.0.0.1:3000/");

        authorizationPage.clickLogInButton()
                .setUsername(userForTest.username())
                .setPassword(userForTest.testData().password())
                .clickSignInButton();
    }

    @Test
    void checkingFriendInvitationReceiveTest(@User(selector = INVITATION_SENT) UserJson anotherUserForTest) {
        headerPage.openAllPeoplePage()
                .ifThereIsASubmitButton(
                        allPeoplePage
                                .findFriendByUsername(anotherUserForTest.username()))
                .ifThereIsADeclineButton(allPeoplePage
                        .findFriendByUsername(anotherUserForTest.username()));

        headerPage.openFriendsPage()
                .ifThereIsASubmitButton(
                        allPeoplePage
                                .findFriendByUsername(anotherUserForTest.username()))
                .ifThereIsADeclineButton(
                        allPeoplePage
                                .findFriendByUsername(anotherUserForTest.username()));
    }

    @Test
    void checkingFriendInvitationReceiveTest1(@User(selector = INVITATION_SENT) UserJson anotherUserForTest) {
        headerPage.openAllPeoplePage()
                .ifThereIsASubmitButton(
                        friendsPage
                                .findFriendByUsername(anotherUserForTest.username()))
                .ifThereIsADeclineButton(
                        friendsPage
                                .findFriendByUsername(anotherUserForTest.username()));

        headerPage.openFriendsPage()
                .ifThereIsASubmitButton(
                        friendsPage
                                .findFriendByUsername(anotherUserForTest.username()))
                .ifThereIsADeclineButton(
                        friendsPage
                                .findFriendByUsername(anotherUserForTest.username()));
    }

    @Test
    void checkingFriendInvitationReceiveTest2(@User(selector = INVITATION_SENT) UserJson anotherUserForTest) {
        headerPage.openAllPeoplePage()
                .ifThereIsASubmitButton(
                        friendsPage
                                .findFriendByUsername(anotherUserForTest.username()))
                .ifThereIsADeclineButton(
                        friendsPage
                                .findFriendByUsername(anotherUserForTest.username()));

        headerPage.openFriendsPage()
                .ifThereIsASubmitButton(
                        friendsPage
                                .findFriendByUsername(anotherUserForTest.username()))
                .ifThereIsADeclineButton(
                        friendsPage
                                .findFriendByUsername(anotherUserForTest.username()));
    }
}
