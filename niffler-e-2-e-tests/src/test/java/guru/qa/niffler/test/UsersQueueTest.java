package guru.qa.niffler.test;

import com.codeborne.selenide.*;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.UserType;
import guru.qa.niffler.pages.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WindowType;


@WebTest
@ExtendWith(UserQueueExtension.class)
public class UsersQueueTest {

    static {
        Configuration.browserSize = "1920x1080";
    }

    StartPage startPage = new StartPage();
    AuthPage authPage = new AuthPage();
    MainPage mainPage = new MainPage();
    FriendsPage friendsPage = new FriendsPage();
    PeoplePage peoplePage = new PeoplePage();


    @Test
    void noFriendsInTable(@User(UserType.COMMON) UserJson testUser) {

        Selenide.open("http://127.0.0.1:3000/");
        startPage.clickLoginButton();
        authPage.login(testUser.username(), testUser.testData().password());
        mainPage.clickFriendsButton();
        friendsPage.checkNoFriendsMessage();
    }

    @Test
    void friendsSeeEachOtherInFiendsTable(@User(UserType.WITH_FRIEND) UserJson testUser,
                                          @User(UserType.WITH_FRIEND_2) UserJson testUserAnother) {

        Selenide.open("http://127.0.0.1:3000/");
        startPage.clickLoginButton();
        authPage.login(testUser.username(), testUser.testData().password());
        mainPage.clickFriendsButton();
        friendsPage.checkFriendsName(testUser.testData().friend().getFirst());
        mainPage.logOut();

        Selenide.switchTo().newWindow(WindowType.WINDOW);
        Selenide.open("http://127.0.0.1:3000/");
        startPage.clickLoginButton();
        authPage.login(testUserAnother.username(), testUserAnother.testData().password());
        mainPage.clickFriendsButton();
        friendsPage.checkFriendsName(testUserAnother.testData().friend().getFirst());
    }

    @Test
    void userSendInviteAndUserInvitedSeeEachOtherInFriendsTable(@User(UserType.WITH_FRIEND) UserJson userSendInvite,
                                                                @User(UserType.WITH_INVITE) UserJson userWithInvite) {

        Selenide.open("http://127.0.0.1:3000/");
        startPage.clickLoginButton();
        authPage.login(userSendInvite.username(), userSendInvite.testData().password());
        mainPage.clickPeopleButton();
        peoplePage.checkStatus("Nastiletko", "Pending invitation");
        mainPage.logOut();

        Selenide.switchTo().newWindow(WindowType.WINDOW);
        Selenide.open("http://127.0.0.1:3000/");
        startPage.clickLoginButton();
        authPage.login(userWithInvite.username(), userWithInvite.testData().password());
        mainPage.clickFriendsButton();
        friendsPage.checkFriendsName(userSendInvite.username());
    }

    @Test
    void userWithInviteCanDeclineAndSubmit(@User(UserType.WITH_INVITE) UserJson testUser) {

        Selenide.open("http://127.0.0.1:3000/");
        startPage.clickLoginButton();
        authPage.login(testUser.username(), testUser.testData().password());
        mainPage.clickFriendsButton();
        friendsPage.checkOptionInvite("decline").checkOptionInvite("submit");
    }

    @Test
    void friendTaggedInPeopleTable(@User(UserType.WITH_FRIEND) UserJson testUser) {

        Selenide.open("http://127.0.0.1:3000/");
        startPage.clickLoginButton();
        authPage.login(testUser.username(), testUser.testData().password());
        mainPage.clickPeopleButton();
        peoplePage.checkStatus("Nastiletko2", "You are friends");
    }
}