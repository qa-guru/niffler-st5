package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class AuthorizationPage {

	private final SelenideElement
			loginButton = $x("//a[contains(text(),'Login')]"),
			registerButton = $x("//a[contains(text(),'Register')]");

	public void loginButtonClick(){
		loginButton.click();
	}
	public void loginButtonIsVisible(){
		loginButton.should(visible);
	}
	public void registerButtonClick(){
		registerButton.click();
	}

}
