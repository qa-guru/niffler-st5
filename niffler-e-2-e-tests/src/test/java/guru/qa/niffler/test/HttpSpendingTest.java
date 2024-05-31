package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.annotation.meta.WebTestHttp;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.AuthorizationPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;
import java.util.Objects;


@WebTestHttp
public class HttpSpendingTest {

	AuthorizationPage authorizationPage = new AuthorizationPage();

	MainPage mainPage = new MainPage();

	LoginPage loginPage = new LoginPage();


	static {
		Configuration.browserSize = "1920x1080";
	}

	@BeforeEach
	void doLogin(UserJson userJson) {
		Selenide.open("http://127.0.0.1:3000/");
		authorizationPage.clickLoginButton();
		loginPage.userNameFieldSetValue(userJson.username());
		loginPage.passwordFieldSetValue(userJson.testData().password());
		loginPage.signUpClick();

	}

	@Test
	void anotherTest() {
		Selenide.open("http://127.0.0.1:3000/");
		authorizationPage.loginButtonIsVisible();
	}

	@AfterEach
	void doScreenshot() {
		Allure.addAttachment(
				"Screen on test end",
				new ByteArrayInputStream(
						Objects.requireNonNull(
								Selenide.screenshot(OutputType.BYTES)
						)
				)
		);
	}

	@TestUser
	@GenerateCategory
	@GenerateSpend
	@Test
	void spendingShouldBeDeletedAfterTableAction(SpendJson spendJson) {
		mainPage
				.selectSpending(spendJson.description())
				.clickDeleteSelectButton()
				.checkListSpendingIsEmpty();
	}
}
