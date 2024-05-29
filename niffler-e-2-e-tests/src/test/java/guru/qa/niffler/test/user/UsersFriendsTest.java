package guru.qa.niffler.test.user;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.FriendsBrowsePage;
import guru.qa.niffler.pages.LoginPage;
import guru.qa.niffler.pages.PeopleBrowsePage;
import guru.qa.niffler.pages.WelcomePage;
import guru.qa.niffler.pages.common.HeaderMenu;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static guru.qa.niffler.jupiter.annotation.User.UserType.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebTest
public class UsersFriendsTest {

    private final WelcomePage welcomePage = new WelcomePage();
    private final LoginPage loginPage = new LoginPage();
    private final HeaderMenu menu = new HeaderMenu();
    private final FriendsBrowsePage friendsPage = new FriendsBrowsePage();
    private final PeopleBrowsePage peoplePage = new PeopleBrowsePage();

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000");
        welcomePage.goToLogin();
    }

    //**************** INVITATION SEND ********************//

    @Test
    void invitationSendTest0(@User(INVITATION_SEND) UserJson invitationSendTestUser) {
        loginPage.login(invitationSendTestUser.username(), invitationSendTestUser.testData().password());
        menu.getAvatar().should(exist);

        menu.getPeople().click();
        peoplePage.getPeopleContentTableWrapper().should(visible);
        peoplePage.getFriends().find(text("Pending invitation")).should(exist);
    }

    // для каждого типа по 3 теста для создания очереди, можно скролить до 172 строки
    @Test
    void invitationSendTest1(@User(INVITATION_SEND) UserJson invitationSendTestUser) {
        loginPage.login(invitationSendTestUser.username(), invitationSendTestUser.testData().password());

        menu.getAvatar().should(exist);

        menu.getPeople().click();
        peoplePage.getPeopleContentTableWrapper().should(visible);
        peoplePage.getFriends().find(text("Pending invitation")).should(exist);
    }

    @Test
    void invitationSendTest2(@User(INVITATION_SEND) UserJson invitationSendTestUser) {
        loginPage.login(invitationSendTestUser.username(), invitationSendTestUser.testData().password());
        menu.getAvatar().should(exist);

        menu.getPeople().click();
        peoplePage.getPeopleContentTableWrapper().should(visible);
        peoplePage.getFriends().find(text("Pending invitation")).should(exist);
    }

    //**************** INVITATION RECIEVED ****************//

    @Test
    void invitationRecievedTest0(@User(INVITATION_RECEIVED) UserJson
                                         invitationRecievedTestUser) {

        loginPage.login(invitationRecievedTestUser.username(), invitationRecievedTestUser.testData().password());
        menu.getAvatar().should(exist);

        menu.getFriends().click();
        friendsPage.getPeopleContentTableWrapper().should(exist);
        friendsPage.getSubmitInvitationBtn().should(exist);
        friendsPage.getDeclineInvitationBtn().should(exist);

        menu.getPeople().click();
        peoplePage.getPeopleContentTableWrapper().should(visible);
        peoplePage.getSubmitInvitationBtn().should(exist);
        peoplePage.getDeclineInvitationBtn().should(exist);
    }

    @Test
    void invitationRecievedTest1(@User(INVITATION_RECEIVED) UserJson
                                         invitationRecievedTestUser) {
        loginPage.login(invitationRecievedTestUser.username(), invitationRecievedTestUser.testData().password());
        menu.getAvatar().should(exist);

        menu.getFriends().click();
        friendsPage.getPeopleContentTableWrapper().should(exist);
        friendsPage.getSubmitInvitationBtn().should(exist);
        friendsPage.getDeclineInvitationBtn().should(exist);

        menu.getPeople().click();
        peoplePage.getPeopleContentTableWrapper().should(visible);
        peoplePage.getSubmitInvitationBtn().should(exist);
        peoplePage.getDeclineInvitationBtn().should(exist);
    }

    @Test
    void invitationRecievedTest2(@User(INVITATION_RECEIVED) UserJson
                                         invitationRecievedTestUser) {
        loginPage.login(invitationRecievedTestUser.username(), invitationRecievedTestUser.testData().password());

        menu.getAvatar().should(exist);

        menu.getFriends().click();
        friendsPage.getPeopleContentTableWrapper().should(exist);

        friendsPage.getSubmitInvitationBtn().should(exist);
        friendsPage.getDeclineInvitationBtn().should(exist);

        menu.getPeople().click();
        peoplePage.getPeopleContentTableWrapper().should(visible);
        peoplePage.getDeclineInvitationBtn().should(exist);
        peoplePage.getSubmitInvitationBtn().should(exist);
    }

    //******************** WITH FRIENDS *********************//

    @Test
    void withFiendsTest0(@User(WITH_FRIENDS) UserJson withFriendsTestUser) {
        loginPage.login(withFriendsTestUser.username(), withFriendsTestUser.testData().password());
        menu.getAvatar().should(exist);

        menu.getPeople().click();
        friendsPage.getPeopleContentTableWrapper().should(exist);
        friendsPage.getFriends().find(text("You are friends")).should(exist);
    }

    @Test
    void withFiendsTest1(@User(WITH_FRIENDS) UserJson withFriendsTestUser) {
        loginPage.login(withFriendsTestUser.username(), withFriendsTestUser.testData().password());
        menu.getAvatar().should(exist);

        menu.getFriends().click();
        friendsPage.getPeopleContentTableWrapper().should(exist);
        friendsPage.getFriends().find(text("You are friends")).should(exist);
    }

    @Test
    void withFiendsTest2(@User(WITH_FRIENDS) UserJson withFriendsTestUser) {
        loginPage.login(withFriendsTestUser.username(), withFriendsTestUser.testData().password());
        menu.getAvatar().should(exist);

        menu.getFriends().click();
        friendsPage.getPeopleContentTableWrapper().should(exist);
        friendsPage.getFriends().find(text("You are friends")).should(exist);
    }

    //**************** Multiple users ********************//

    @DisplayName("Разные юзеры в контексте")
    @Test
    void withDifferentUserTypesTest(
            @User(WITH_FRIENDS) UserJson user1,
            @User(INVITATION_RECEIVED) UserJson user2) {
        assertTrue(user1.username().contains("_friend"));
        assertTrue(user2.username().contains("_received"));
    }

    @DisplayName("Каждой твари в контексте по паре")
    @Description("Два пользователя одного типа. Параметры в тестовом методе")
    @Test
    void withSameUserTypesTest(
            @User(INVITATION_SEND) UserJson user1,
            @User(INVITATION_SEND) UserJson user2) {
        assertTrue(user1.username().contains("_send"));
        assertTrue(user2.username().contains("_send"));
        assertNotEquals(user1.username(), user2.username());
    }

    @Disabled
    @Description("Перегружает запрос ресурсов. Тесты которые застряли упадут через 30 сек.")
    @DisplayName("Слишком много каждой твари в контексте по паре.")
    @Test()
    void withFiendsInvitationSendTest3(
            @User(WITH_FRIENDS) UserJson user1,
            @User(WITH_FRIENDS) UserJson user2,
            @User(WITH_FRIENDS) UserJson user3) {
    }
}