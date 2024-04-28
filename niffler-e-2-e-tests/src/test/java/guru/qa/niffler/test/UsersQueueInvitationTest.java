package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.page.WelcomePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.*;
import static guru.qa.niffler.jupiter.annotation.User.Selector.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(UsersQueueExtension.class)
public class UsersQueueInvitationTest {

    PeoplePage peoplePage = new PeoplePage();

    @BeforeEach
    void setup() {

        WelcomePage welcomePage = new WelcomePage();
        LoginPage loginPage = new LoginPage();


        Selenide.open("http://127.0.0.1:3000/");
        welcomePage.clickLoginBtn();
        loginPage.doLogin("Aleksei", "Pass123");
    }

    @Test
    void friendsTestWithFriendAndInviteSent(@User(selector = FRIEND) UserJson userForTest,
                                @User(selector = INVITE_SENT) UserJson userForAnotherTest) {
        Selenide.open("http://127.0.0.1:3000/people");
        assertTrue(peoplePage.isFriend(userForTest.username()));

        assertTrue(peoplePage.isInviteSent(userForAnotherTest.username()));

    }

    @Test
    void friendsTestWithFriendAndInviteReceived(@User(selector = FRIEND) UserJson userForTest,
                                            @User(selector = INVITE_RECEIVED) UserJson userForAnotherTest) {
        Selenide.open("http://127.0.0.1:3000/people");
        assertTrue(peoplePage.isFriend(userForTest.username()));

        assertTrue(peoplePage.isInvitationReceived(userForAnotherTest.username()));

    }

    @Test
    void friendsTestWithInviteSentAndInviteReceived(@User(selector = INVITE_SENT) UserJson userForTest,
                                                @User(selector = INVITE_RECEIVED) UserJson userForAnotherTest) {
        Selenide.open("http://127.0.0.1:3000/people");
        assertTrue(peoplePage.isInviteSent(userForTest.username()));

        assertTrue(peoplePage.isInvitationReceived(userForAnotherTest.username()));

    }
}
