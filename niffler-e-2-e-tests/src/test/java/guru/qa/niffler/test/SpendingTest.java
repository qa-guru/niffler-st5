package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.jupiter.extension.SpendExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.AuthorizationPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;
import java.util.Objects;


@ExtendWith({CategoryExtension.class, SpendExtension.class})
public class SpendingTest {

	AuthorizationPage authorizationPage = new AuthorizationPage();

	MainPage mainPage = new MainPage();

	LoginPage loginPage = new LoginPage();

	private static final String category = "Обучение";
	private static final String userName = "dima";
	private static final String userPassword = "12345";


	static {
		Configuration.browserSize = "1920x1080";
	}

	@BeforeEach
	void doLogin() {
		Selenide.open("http://127.0.0.1:3000/");
		authorizationPage.loginButtonClick();
		loginPage.userNameFieldSetValue(userName);
		loginPage.passwordFieldSetValue(userPassword);
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
			category = category,
			username = userName
	)
	@GenerateSpend(
			username = userName,
			category = category,
			description = "QA.GURU Advanced 5",
			amount = 65000.00,
			currency = CurrencyValues.RUB
	)
	@Test
	void spendingShouldBeDeletedAfterTableAction(SpendJson spendJson) {
		mainPage.selectSpending(spendJson.description());
		mainPage.clickDeleteSelectButton();
		mainPage.checkListSpendingIsEmpty();
	}
}
