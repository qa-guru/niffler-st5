package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.FriendsBrowsePage;
import guru.qa.niffler.pages.LoginPage;
import guru.qa.niffler.pages.PeopleBrowsePage;
import guru.qa.niffler.pages.WelcomePage;
import guru.qa.niffler.pages.common.HeaderMenu;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.User.UserType.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebTest
public class UsersFriendsTest {

    private final WelcomePage welcomePage = new WelcomePage();
    private final LoginPage loginPage = new LoginPage();
    private final HeaderMenu menu = new HeaderMenu();
    private final FriendsBrowsePage friends = new FriendsBrowsePage();
    private final PeopleBrowsePage people = new PeopleBrowsePage();

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000");
        welcomePage.goToLogin();
    }

    //**************** INVITATION SEND ********************//

    @Test
    void invitationSendTest0(@User(INVITATION_SEND) UserJson invitationSendTestUser) {
        loginPage.login(invitationSendTestUser.username(), invitationSendTestUser.testData().password());

        assertTrue(menu.isPageLoaded(), "Бро, я не вижу аватар");

        menu.openPeopleList();

        Allure.step("Проверка: Pending invitation", () -> {
            assertTrue(people.checkRowWithStatus("Pending invitation"),
                    "Бро, пора бы пригласить кого-нибудь");
        });
    }

    // для каждого типа по 3 теста для создания очереди, можно скролить до 172 строки
    @Test
    void invitationSendTest1(@User(INVITATION_SEND) UserJson invitationSendTestUser) {
        loginPage.login(invitationSendTestUser.username(), invitationSendTestUser.testData().password());

        assertTrue(menu.isPageLoaded(), "Бро, я не вижу аватар");

        menu.openPeopleList();

        assertTrue(people.checkRowWithStatus("Pending invitation"),
                "Бро, пора бы пригласить кого-нибудь");
    }

    @Test
    void invitationSendTest2(@User(INVITATION_SEND) UserJson invitationSendTestUser) {
        loginPage.login(invitationSendTestUser.username(), invitationSendTestUser.testData().password());

        assertTrue(menu.isPageLoaded(), "Бро, я не вижу аватар");

        menu.openPeopleList();

        assertTrue(people.checkRowWithStatus("Pending invitation"),
                "Бро, пора бы пригласить кого-нибудь");
    }

    //**************** INVITATION RECIEVED ****************//

    @Test
    void invitationRecievedTest0(@User(INVITATION_RECEIVED) UserJson
                                         invitationRecievedTestUser) {
        loginPage.login(invitationRecievedTestUser.username(), invitationRecievedTestUser.testData().password());

        assertTrue(menu.isPageLoaded(), "Бро, я не вижу аватар");

        menu.openFriendsList();

        assertTrue(friends.checkInvitations(),
                "Бро, сорян, сегодня новых друзей не будет :(");

        menu.openPeopleList();

        assertTrue(people.checkInvitations(),
                "Тут тоже без новых друзей, прости :(");
    }

    @Test
    void invitationRecievedTest1(@User(INVITATION_RECEIVED) UserJson
                                         invitationRecievedTestUser) {
        loginPage.login(invitationRecievedTestUser.username(), invitationRecievedTestUser.testData().password());

        assertTrue(menu.isPageLoaded(), "Бро, я не вижу аватар");

        menu.openFriendsList();

        assertTrue(friends.checkInvitations(),
                "Бро, сорян, сегодня новых друзей не будет :(");

        menu.openPeopleList();

        assertTrue(people.checkInvitations(),
                "Тут тоже без новых друзей, прости :(");
    }

    @Test
    void invitationRecievedTest2(@User(INVITATION_RECEIVED) UserJson
                                         invitationRecievedTestUser) {
        loginPage.login(invitationRecievedTestUser.username(), invitationRecievedTestUser.testData().password());

        assertTrue(menu.isPageLoaded(), "Бро, я не вижу аватар");

        menu.openFriendsList();

        assertTrue(friends.checkInvitations(),
                "Бро, сорян, сегодня новых друзей не будет :(");

        menu.openPeopleList();

        assertTrue(people.checkInvitations(),
                "Тут тоже без новых друзей, прости :(");
    }

    //******************** WITH FRIENDS *********************//

    @Test
    void withFiendsTest0(@User(WITH_FRIENDS) UserJson withFriendsTestUser) {
        loginPage.login(withFriendsTestUser.username(), withFriendsTestUser.testData().password());

        assertTrue(menu.isPageLoaded(), "Бро, я не вижу аватар");

        menu.openFriendsList();

        assertTrue(friends.checkRowWithStatus("You are friends"),
                "Бро, ты, походу, лузер. У тебя совсем нет друзей!");
    }

    @Test
    void withFiendsTest1(@User(WITH_FRIENDS) UserJson withFriendsTestUser) {
        loginPage.login(withFriendsTestUser.username(), withFriendsTestUser.testData().password());

        assertTrue(menu.isPageLoaded(), "Бро, я не вижу аватар");

        menu.openFriendsList();

        assertTrue(friends.checkRowWithStatus("You are friends"),
                "Бро, ты, походу, лузер. У тебя совсем нет друзей!");
    }

    @Test
    void withFiendsTest2(@User(WITH_FRIENDS) UserJson withFriendsTestUser) {
        loginPage.login(withFriendsTestUser.username(), withFriendsTestUser.testData().password());

        assertTrue(menu.isPageLoaded(), "Бро, я не вижу аватар");

        menu.openFriendsList();

        assertTrue(friends.checkRowWithStatus("You are friends"),
                "Бро, ты, походу, лузер. У тебя совсем нет друзей!");
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
    @Description("Тест упадёт. Необходимо доработать резолвер. Пока не понял как.")
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