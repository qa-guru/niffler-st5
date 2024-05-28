package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$x;

public class PeoplePage {
	private SelenideElement actionUser(String userName) {
		return $x(String.format("//td[contains(text(),'%s')]/..//button[1]", userName));
	}

	@Step("Нажать действие у пользователя {userName}")
	public PeoplePage clickActionFromUser(String userName) {
		actionUser(userName).click();
		return this;
	}
}
