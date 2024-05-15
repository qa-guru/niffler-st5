package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.enums.Alert.*;
import static guru.qa.niffler.jupiter.annotation.User.UserType.*;

@WebTest
@ExtendWith({
		UserQueueExtension.class
})
public class PeopleTest {

	MainPage mainPage = new MainPage();
	Common common = new Common();
	AuthorizationPage authorizationPage = new AuthorizationPage();

	void doLogin(UserJson user) {
		Selenide.open("http://127.0.0.1:3000/");
		authorizationPage.
				clickLoginButton().
				userNameFieldSetValue(user.username()).
				passwordFieldSetValue(user.testData().password()).
				signUpClick();
	}

	@Test
	public void sendInvitationFriend(
			@User(userType = INVITE_SENT) UserJson userInviteSend,
			@User(userType = FRIEND) UserJson userFriend
	) {
		doLogin(userFriend);
		mainPage.
				clickAllPeopleButton().
				clickActionFromUser(userInviteSend.username());
		common.checkAlert(INVITATION_IS_SENT);
	}

	@Test
	public void submitInvitationFriend(
			@User(userType = FRIEND) UserJson userFriend,
			@User(userType = INVITE_RECEIVED) UserJson userInviteReceived
	){
		doLogin(userFriend);
		mainPage.
				clickFriendsButton().
				clickSubmit(userInviteReceived.username());
		common.checkAlert(INVITATION_IS_ACCEPTED);
	}
	@Test
	public void declineInvitationFriend(
			@User(userType = FRIEND) UserJson userFriend,
			@User(userType = INVITE_RECEIVED) UserJson userInviteReceived
	){
		doLogin(userFriend);
		mainPage.
				clickFriendsButton().
				clickDecline(userInviteReceived.username());
		common.checkAlert(INVITATION_IS_DECLINED);
	}
	@Test
	public void deleteFriend(
			@User(userType = FRIEND) UserJson userFriend,
			@User(userType = FRIEND) UserJson userFriend1
	){
		doLogin(userFriend);
		mainPage.
				clickFriendsButton().
				clickDelete(userFriend1.username());
		common.checkAlert(FRIEND_IS_DELETED);
	}
}
