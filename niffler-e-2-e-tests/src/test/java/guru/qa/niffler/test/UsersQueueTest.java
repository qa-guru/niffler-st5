package guru.qa.niffler.test;

import com.codeborne.selenide.*;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


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
    void noFriendsInTable(@User() UserJson testUser) {

        startPage.openPage()
                .clickLoginButton();
        authPage.login(testUser.username(), testUser.testData().password());
        mainPage.clickFriendsButton();
        friendsPage.checkNoFriendsMessage();
    }

    @Test
    void friendsSeeEachOtherInFiendsTable(@User(User.UserType.WITH_FRIEND) UserJson testUser,
                                          @User(User.UserType.WITH_FRIEND) UserJson testUserAnother) {

        startPage.openPage()
                .clickLoginButton();
        authPage.login(testUser.username(), testUser.testData().password());
        mainPage.clickFriendsButton();
        friendsPage.checkFriendsName(testUserAnother.username());
    }

    @Test
    void userSendInviteSeeInvitedInFriendsTable(@User(User.UserType.INVITATION_SEND) UserJson userSendInvite,
                                                @User(User.UserType.INVITATION_RECIEVED) UserJson userWithInvite) {

        startPage.openPage()
                .clickLoginButton();
        startPage.clickLoginButton();
        authPage.login(userSendInvite.username(), userSendInvite.testData().password());
        mainPage.clickPeopleButton();
        peoplePage.checkStatus(userWithInvite.username(), "Pending invitation");
    }

    @Test
    void userWithInviteSeeInvitedInFriendsTable(@User(User.UserType.INVITATION_SEND) UserJson userSendInvite,
                                                @User(User.UserType.INVITATION_RECIEVED) UserJson userWithInvite) {

        startPage.openPage()
                .clickLoginButton();
        startPage.clickLoginButton();
        authPage.login(userWithInvite.username(), userWithInvite.testData().password());
        mainPage.clickFriendsButton();
        friendsPage.checkFriendsName(userSendInvite.username());
    }

    @Test
    void userWithInviteCanDeclineAndSubmit(@User(User.UserType.INVITATION_RECIEVED) UserJson testUser) {

        startPage.openPage()
                .clickLoginButton();
        startPage.clickLoginButton();
        authPage.login(testUser.username(), testUser.testData().password());
        mainPage.clickFriendsButton();
        friendsPage.checkOptionInvite("decline").checkOptionInvite("submit");
    }

    @Test
    void friendTaggedInPeopleTable(@User(User.UserType.WITH_FRIEND) UserJson testUser,
                                   @User(User.UserType.WITH_FRIEND) UserJson testUserAnother) {

        startPage.openPage()
                .clickLoginButton();
        startPage.clickLoginButton();
        authPage.login(testUser.username(), testUser.testData().password());
        mainPage.clickPeopleButton();
        peoplePage.checkStatus(testUserAnother.username(), "You are friends");
    }
}