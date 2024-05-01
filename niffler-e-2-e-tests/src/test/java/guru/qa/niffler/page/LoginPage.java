package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class LoginPage {

	private final SelenideElement
			userNameField = $x("//input[@name='username']"),
			passwordField = $x("//input[@name='password']"),
			signUp = $x("//button[contains(text(),'Sign In')]");

	public LoginPage userNameFieldSetValue(String userName) {
		userNameField.setValue(userName);
		return this;
	}

	public LoginPage passwordFieldSetValue(String password) {
		passwordField.setValue(password);
		return this;
	}

	public void signUpClick() {
		signUp.click();
	}

}
