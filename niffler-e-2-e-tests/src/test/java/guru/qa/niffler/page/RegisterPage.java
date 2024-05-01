package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class RegisterPage {
	private final SelenideElement
			userNameField = $x("//input[@id='username']"),
			passwordField = $x("//input[@id='password']"),
			passwordSubmitField = $x("//input[@id='passwordSubmit']"),
			signUp = $x("//button[contains(text(),'Sign Up')]");

	public RegisterPage userNameFieldSetValue(String userName) {
		userNameField.setValue(userName);
		return this;
	}

	public RegisterPage passwordFieldSetValue(String password) {
		passwordField.setValue(password);
		passwordSubmitField.setValue(password);
		return this;
	}

	public void signUpClick() {
		signUp.click();
	}
}
