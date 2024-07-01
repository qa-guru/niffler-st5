package guru.qa.niffler.test.parallelWeb;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.BaseWebTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.jupiter.annotation.User.UserType.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Друзья")
@WebTest
@Tag("ParallelWebTests")
@Execution(ExecutionMode.SAME_THREAD)
public class UsersFriendsTest extends BaseWebTest {

    @BeforeEach
    void doLogin() {
        open(CFG.frontUrl());
        welcomePage.goToLogin();
    }

    //**************** INVITATION SEND ********************//

    @Test
    void invitationSendTest0(@User(INVITATION_SEND) UserJson invitationSendTestUser) {
        loginPage.login(invitationSendTestUser.username(), invitationSendTestUser.testData().password());
        mainPage.waitForPageLoaded();

        menu.openPeoplePage();
        peoplePage.waitForPageLoaded();
        peoplePage.getPeopleContentTableWrapper().should(visible);
        peoplePage.getFriends().find(text("Pending invitation")).should(exist);
    }

    // для каждого типа по 3 теста для создания очереди, можно скролить до 172 строки
    @Test
    void invitationSendTest1(@User(INVITATION_SEND) UserJson invitationSendTestUser) {
        loginPage.login(invitationSendTestUser.username(), invitationSendTestUser.testData().password());

        mainPage.waitForPageLoaded();

        menu.openPeoplePage();
        peoplePage.waitForPageLoaded();
        peoplePage.getPeopleContentTableWrapper().should(visible);
        peoplePage.getFriends().find(text("Pending invitation")).should(exist);
    }

    @Test
    void invitationSendTest2(@User(INVITATION_SEND) UserJson invitationSendTestUser) {
        loginPage.login(invitationSendTestUser.username(), invitationSendTestUser.testData().password());
        mainPage.waitForPageLoaded();

        menu.openPeoplePage();
        peoplePage.waitForPageLoaded();
        peoplePage.getPeopleContentTableWrapper().should(visible);
        peoplePage.getFriends().find(text("Pending invitation")).should(exist);
    }

    //**************** INVITATION RECIEVED ****************//

    @Test
    void invitationRecievedTest0(@User(INVITATION_RECEIVED) UserJson
                                         invitationRecievedTestUser) {

        loginPage.login(invitationRecievedTestUser.username(), invitationRecievedTestUser.testData().password());
        mainPage.waitForPageLoaded();

        menu.openFriendsPage();
        friendsPage.waitForPageLoaded();
        friendsPage.getPeopleContentTableWrapper().should(exist);
        friendsPage.getPeopleTable().getSubmitInvitationBtn().should(exist);
        friendsPage.getPeopleTable().getDeclineInvitationBtn().should(exist);

        menu.openPeoplePage();
        peoplePage.waitForPageLoaded();
        peoplePage.getPeopleContentTableWrapper().should(visible);
        peoplePage.getSubmitInvitationBtn().should(exist);
        peoplePage.getDeclineInvitationBtn().should(exist);
    }

    @Test
    void invitationRecievedTest1(@User(INVITATION_RECEIVED) UserJson
                                         invitationRecievedTestUser) {
        loginPage.login(invitationRecievedTestUser.username(), invitationRecievedTestUser.testData().password());
        mainPage.waitForPageLoaded();

        menu.openFriendsPage();
        friendsPage.waitForPageLoaded();
        friendsPage.getPeopleContentTableWrapper().should(exist);
        friendsPage.getPeopleTable().getSubmitInvitationBtn().should(exist);
        friendsPage.getPeopleTable().getDeclineInvitationBtn().should(exist);

        menu.openPeoplePage();
        peoplePage.waitForPageLoaded();
        peoplePage.getPeopleContentTableWrapper().should(visible);
        peoplePage.getSubmitInvitationBtn().should(exist);
        peoplePage.getDeclineInvitationBtn().should(exist);
    }

    @Test
    void invitationRecievedTest2(@User(INVITATION_RECEIVED) UserJson
                                         invitationRecievedTestUser) {
        loginPage.login(invitationRecievedTestUser.username(), invitationRecievedTestUser.testData().password());

        mainPage.waitForPageLoaded();

        menu.openFriendsPage();
        friendsPage.waitForPageLoaded();
        friendsPage.getPeopleContentTableWrapper().should(exist);

        friendsPage.getPeopleTable().getSubmitInvitationBtn().should(exist);
        friendsPage.getPeopleTable().getDeclineInvitationBtn().should(exist);

        menu.openPeoplePage();
        peoplePage.waitForPageLoaded();
        peoplePage.getPeopleContentTableWrapper().should(visible);
        peoplePage.getDeclineInvitationBtn().should(exist);
        peoplePage.getSubmitInvitationBtn().should(exist);
    }

    //******************** WITH FRIENDS *********************//

    @Test
    void withFiendsTest0(@User(WITH_FRIENDS) UserJson withFriendsTestUser) {
        loginPage.login(withFriendsTestUser.username(), withFriendsTestUser.testData().password());
        mainPage.waitForPageLoaded();

        menu.openPeoplePage();
        peoplePage.waitForPageLoaded();
        friendsPage.getPeopleContentTableWrapper().should(exist);
        friendsPage.getPeopleTable().getAllRows().find(text("You are friends")).should(exist);
    }

    @Test
    void withFiendsTest1(@User(WITH_FRIENDS) UserJson withFriendsTestUser) {
        loginPage.login(withFriendsTestUser.username(), withFriendsTestUser.testData().password());
        mainPage.waitForPageLoaded();

        menu.openFriendsPage();
        friendsPage.waitForPageLoaded();
        friendsPage.getPeopleContentTableWrapper().should(exist);
        friendsPage.getPeopleTable().getAllRows().find(text("You are friends")).should(exist);
    }

    @Test
    void withFiendsTest2(@User(WITH_FRIENDS) UserJson withFriendsTestUser) {
        loginPage.login(withFriendsTestUser.username(), withFriendsTestUser.testData().password());
        mainPage.waitForPageLoaded();

        menu.openFriendsPage();
        friendsPage.waitForPageLoaded();
        friendsPage.getPeopleContentTableWrapper().should(exist);
        friendsPage.getPeopleTable().getAllRows().find(text("You are friends")).should(exist);
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