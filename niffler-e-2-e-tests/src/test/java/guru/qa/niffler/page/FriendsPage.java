package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$x;

public class FriendsPage {
	private SelenideElement declineButton(String userName) {
		return $x(String.format("(//td[contains(text(),'%s')]/..//button[1])[2]", userName));
	}
	private SelenideElement deleteButton(String userName) {
		return $x(String.format("//td[contains(text(),'%s')]/..//button[1]", userName));
	}
	private SelenideElement submitButton(String userName) {
		return $x(String.format("//td[contains(text(),'%s')]/..//button[contains(@class,'submit')]", userName));
	}

	@Step("Принять приглашение")
	public void clickSubmit(String userName){
		submitButton(userName).click();
	}
	@Step("Отклонить приглашение")
	public void clickDecline(String userName){
		declineButton(userName).click();
	}
	@Step("Удалить друга")
	public void clickDelete(String userName){
		deleteButton(userName).click();
	}
}
