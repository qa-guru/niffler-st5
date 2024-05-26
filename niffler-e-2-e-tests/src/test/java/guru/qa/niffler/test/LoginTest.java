package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.UserRepositoryJdbc;
import guru.qa.niffler.data.repository.UserRepositoryStringJdbc;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.DbCreateUserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.AuthorizationPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static guru.qa.niffler.enums.Authority.write;

@ExtendWith({BrowserExtension.class, DbCreateUserExtension.class})
public class LoginTest {
	AuthorizationPage authorizationPage = new AuthorizationPage();

	MainPage mainPage = new MainPage();

	LoginPage loginPage = new LoginPage();

	ProfilePage profilePage = new ProfilePage();

	UserRepositoryJdbc userRepositoryJdbc = new UserRepositoryJdbc();

	UserRepositoryStringJdbc userRpStringJdbc = new UserRepositoryStringJdbc();

	@BeforeEach
	void openPage() {
		Selenide.open("http://127.0.0.1:3000/");
	}

	private void doLogin(UserJson userJson) {
		authorizationPage.clickLoginButton();
		loginPage.userNameFieldSetValue(userJson.username());
		loginPage.passwordFieldSetValue(userJson.testData().password());
		loginPage.signUpClick();
	}

	@Test
	@TestUser
	public void loginNewUser(UserJson userJson) {
		doLogin(userJson);
		mainPage.checkTitleIsVisible();
	}

	@Test
	@TestUser
	public void loginUserAfterEditByJdbc(UserJson userJson) {
		UserJson editUser = UserJson.dataFromUser(userJson.username());
		UserAuthEntity newAuthEntity = new UserAuthEntity().testUserFromJson(editUser);
		userRepositoryJdbc.updateUserInAuth(newAuthEntity, List.of(write));
		userRepositoryJdbc.updateUserInUserdata(new UserEntity().fromUserJson(editUser));
		doLogin(editUser);
		mainPage.clickProfileButton();
		profilePage.checkUsername(editUser.username()).
				checkFirstname(editUser.firstname()).
				checkSurname(editUser.surname()).
				checkCurrency(editUser.currency().name());
	}

	@Test
	@TestUser
	public void loginUserAfterEditBySpringJdbc(UserJson userJson) {
		UserJson editUser = UserJson.dataFromUser(userJson.username());
		UserAuthEntity newAuthEntity = new UserAuthEntity().testUserFromJson(editUser);
		userRpStringJdbc.updateUserInAuth(newAuthEntity, List.of(write));
		userRpStringJdbc.updateUserInUserdata(new UserEntity().fromUserJson(editUser));
		doLogin(editUser);
		mainPage.clickProfileButton();
		profilePage.checkUsername(editUser.username()).
				checkFirstname(editUser.firstname()).
				checkSurname(editUser.surname()).
				checkCurrency(editUser.currency().name());
	}

	@Test
	@TestUser
	public void checkUserAfterCreationInDatabase(UserJson userJson) {
		UserEntity createUser = userRepositoryJdbc.findUserInUserDataByID(userJson.id()).get();
		Assertions.assertEquals(userJson.id(), createUser.getId());
		Assertions.assertEquals(userJson.username(), createUser.getUsername());
		Assertions.assertEquals(userJson.currency(), createUser.getCurrency());
		Assertions.assertEquals(userJson.firstname(), createUser.getFirstname());
		Assertions.assertEquals(userJson.surname(), createUser.getSurname());
	}
}
