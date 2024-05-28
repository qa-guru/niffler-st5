package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.UiBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.constant.Friendship.INVITATION_RECEIVED;
import static guru.qa.niffler.constant.Friendship.PENDING_INVITATION;
import static guru.qa.niffler.constant.Friendship.WITH_FRIENDS;

@WebTest
public class UserQueueExampleTest {

    private final UiBot ui = new UiBot();

    @BeforeEach
    void login(@User UserJson user) {
        System.out.println(user); //если здесь будет тот же пользователь, что и в тесте, до afterEach код не дойдет, и тест будет ждать юзера вечно.
        Selenide.open("http://127.0.0.1:3000/main");
    }

    @Test
    void userWithFriendTest(@User(friendship = WITH_FRIENDS) UserJson userWithFriend,
                            @User(friendship = WITH_FRIENDS) UserJson friend) {
        ui.startPage()
                .login(userWithFriend)
                .clickAllPeople()
                .assertThatFriendshipHasStatus(friend, WITH_FRIENDS);
    }

    @Test
    void userWithFriendTest1(@User(friendship = WITH_FRIENDS) UserJson userWithFriend,
                             @User(friendship = WITH_FRIENDS) UserJson friend) {
        ui.startPage()
                .login(userWithFriend)
                .clickAllPeople()
                .assertThatFriendshipHasStatus(friend, WITH_FRIENDS);
    }

    @Test
    void invitationReceivedTest(
            @User(friendship = PENDING_INVITATION) UserJson requester,
            @User(friendship = INVITATION_RECEIVED) UserJson addressee) {
        ui.startPage()
                .login(addressee)
                .clickAllPeople()
                .assertThatSubmitActionIsEnabled(requester)
                .assertThatDeclineActionIsEnabled(requester);
    }

    @Test
    void invitationSentTest(
            @User(friendship = PENDING_INVITATION) UserJson requester,
            @User(friendship = INVITATION_RECEIVED) UserJson addressee) {
        ui.startPage()
                .login(requester)
                .clickAllPeople()
                .assertThatFriendshipHasStatus(addressee, PENDING_INVITATION);
    }

}