package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepositoryJdbc;
import guru.qa.niffler.data.repository.SpendRepositoryStringJdbc;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.meta.WebTestJdbc;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.AuthorizationPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Objects;

@WebTestJdbc
public class JdbcSpendingTest {

	AuthorizationPage authorizationPage = new AuthorizationPage();

	MainPage mainPage = new MainPage();

	LoginPage loginPage = new LoginPage();

	SpendRepositoryJdbc spendRepositoryJdbc = new SpendRepositoryJdbc();
	SpendRepositoryStringJdbc spendRepositoryStringJdbc = new SpendRepositoryStringJdbc();

	private static final String CATEGORY = "new Обучение";
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

	@GenerateSpend(
			description = "QA.GURU Advanced 5",
			amount = 65000.00,
			currency = CurrencyValues.RUB
	)

	@Test
	void spendingShouldBeVisibleAfterCreate(SpendJson spendJson) {
		mainPage.checkSpendingIsVisible(spendJson.description());
	}

	@GenerateCategory(
			category = CATEGORY,
			username = USER_NAME
	)

	@GenerateSpend(
			description = "QA.GURU Advanced 5",
			amount = 65000.00,
			currency = CurrencyValues.RUB
	)
	@Test
	void checkSpendingAfterCreateJdbc(SpendJson spendJson) {
		List<SpendEntity> listSpend = spendRepositoryJdbc.findAllByUsername(spendJson.username());
		for (SpendEntity spend : listSpend) {
			mainPage.checkSpendingIsVisible(spend.getDescription());
		}
	}

	@GenerateCategory(
			category = CATEGORY,
			username = USER_NAME
	)

	@GenerateSpend(
			description = "QA.GURU Advanced 5",
			amount = 65000.00,
			currency = CurrencyValues.RUB
	)
	@Test
	void checkSpendingAfterCreateStringJdbc(SpendJson spendJson) {
		List<SpendEntity> listSpend = spendRepositoryStringJdbc.findAllByUsername(spendJson.username());
		for (SpendEntity spend : listSpend) {
			mainPage.checkSpendingIsVisible(spend.getDescription());
		}
	}
}
