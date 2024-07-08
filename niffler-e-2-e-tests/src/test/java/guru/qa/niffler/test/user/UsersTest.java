package guru.qa.niffler.test.user;

import guru.qa.niffler.jupiter.annotation.DbUser;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.DbCreateUserExtension;
import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.FriendState;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsBrowsePage;
import guru.qa.niffler.page.PeopleBrowsePage;
import guru.qa.niffler.test.BaseWebTest;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sessionStorage;
import static io.qameta.allure.Allure.step;

@ExtendWith({BrowserExtension.class, DbCreateUserExtension.class})
@Epic("Пользователи")
public class UsersTest extends BaseWebTest {

    @Test
    @DbUser
    public void peopleTest(UserJson userJson) {
        open(CFG.frontUrl());
        welcomePage.goToLogin();
        loginPage.login(userJson.username(), userJson.testData().password());
        mainPage.waitForPageLoaded();

        step("API: Отправить приглашение пользователям 1, 2 и 3", () -> {
            gatewayApiClient.sendInvitation(
                    "Bearer " + sessionStorage().getItem("id_token"), new FriendJson(usersTestLogins[0]));

            gatewayApiClient.sendInvitation(
                    "Bearer " + sessionStorage().getItem("id_token"), new FriendJson(usersTestLogins[1]));

            gatewayApiClient.sendInvitation(
                    "Bearer " + sessionStorage().getItem("id_token"), new FriendJson(usersTestLogins[2]));
        });

        menu.logout();

        step("API: пользователем 2 принять предложение", () -> {
            welcomePage.goToLogin();
            loginPage.login(usersTestLogins[1], "12345");

            gatewayApiClient.acceptInvitation(
                    "Bearer " + sessionStorage().getItem("id_token"), new FriendJson(userJson.username()));
        });

        menu.logout();

        step("API: пользователем 3 отклонить предложение", () -> {
            welcomePage.goToLogin();
            loginPage.login(usersTestLogins[2], "12345");

            gatewayApiClient.declineInvitation(
                    "Bearer " + sessionStorage().getItem("id_token"), new FriendJson(userJson.username()));
        });

        menu.logout();

        step("API: пользователем 4 отправить предложение основному пользователю", () -> {
            welcomePage.goToLogin();
            loginPage.login(usersTestLogins[3], "12345");

            gatewayApiClient.sendInvitation(
                    "Bearer " + sessionStorage().getItem("id_token"), new FriendJson(userJson.username()));
        });

        menu.logout();

        welcomePage.goToLogin();
        loginPage.login(userJson.username(), userJson.testData().password());
        mainPage.waitForPageLoaded();
        open(PeopleBrowsePage.URL);

        step("Проверка: на странице 'All People' в списке отображаются все пользователи", () -> {
            List<UserJson> people = gatewayApiClient.allUsers("Bearer " + sessionStorage().getItem("id_token"), "");
            assert people != null;
            peoplePage.getPeopleTable().shouldHavePeople(people.toArray(new UserJson[0]));
        });

        step("Проверка: на странице 'Friends' в списке отображаются добавленные друзья и запросы в друзья", () -> {
            //на страницу друзей попадают друзья
            List<UserJson> friends = gatewayApiClient.allFriends("Bearer " + sessionStorage().getItem("id_token"), "");
            assert friends != null;
            List<UserJson> usersForCheck = new ArrayList<>(friends);

            //на страницу друзей запросы в друзья
            List<UserJson> userInviteReceived =
                    Objects.requireNonNull(gatewayApiClient.allUsers("Bearer " + sessionStorage().getItem("id_token"), ""))
                            .stream()
                            .filter(user -> user.friendState() == FriendState.INVITE_RECEIVED)
                            .toList();

            usersForCheck.addAll(userInviteReceived);
            open(FriendsBrowsePage.URL);
            friendsPage.getPeopleTable().shouldHavePeople(usersForCheck.toArray(new UserJson[0]));
        });
    }

}
