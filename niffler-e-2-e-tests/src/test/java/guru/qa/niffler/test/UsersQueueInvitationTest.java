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
import static guru.qa.niffler.jupiter.annotation.User.Selector.FRIEND;
import static guru.qa.niffler.jupiter.annotation.User.Selector.INVITE_SENT;


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
    void invitationReceivedTest(@User(selector = FRIEND) UserJson userForTest,
                                @User(selector = INVITE_SENT) UserJson userForAnotherTest) {
        Selenide.open("http://127.0.0.1:3000/people");
        SelenideElement userForTestRow = peoplePage.userNameRow(userForTest.username());
        userForTestRow.
                lastChild().
                $("div").
                shouldHave(text("You are friends"));

        SelenideElement userForAnotherTestRow = peoplePage.userNameRow(userForAnotherTest.username());
        userForAnotherTestRow.
                lastChild().
                $(".abstract-table__buttons div").
                shouldHave(attribute(
                        "data-tooltip-id",
                        "submit-invitation"));

    }
}
