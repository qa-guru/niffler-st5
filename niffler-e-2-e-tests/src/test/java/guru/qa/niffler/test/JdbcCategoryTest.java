package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
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

@WebTest
public class JdbcCategoryTest {

	AuthorizationPage authorizationPage = new AuthorizationPage();

	MainPage mainPage = new MainPage();

	LoginPage loginPage = new LoginPage();

	private static final String CATEGORY = "Мое Обучение";
	private static final String USER_NAME = "demidov";
	private static final String USER_PASSWORD = "123456";


	static {
		Configuration.browserSize = "1920x1080";
	}


	@BeforeEach
	void doLogin() {
		Selenide.open("http://127.0.0.1:3000/");
		authorizationPage.clickLoginButton();
		loginPage.userNameFieldSetValue(USER_NAME);
		loginPage.passwordFieldSetValue(USER_PASSWORD);
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

	@GenerateCategory(
			category = CATEGORY,
			username = USER_NAME
	)

	@Test
	void createCategoryByJdbc(CategoryJson category) {
		mainPage.
				clickProfileButton().
				checkCategoriesInList(category.username());
	}
}
