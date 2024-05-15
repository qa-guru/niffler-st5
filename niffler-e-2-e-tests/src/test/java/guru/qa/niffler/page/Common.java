package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.enums.Alert;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class Common {
	private final SelenideElement actionAlert = $x("//div[@class='Toastify']");

	@Step("Проверить алерт после нажатия действия")
	public void checkAlert(Alert alert) {
		actionAlert.
				shouldBe(visible).
				shouldHave(exactText(alert.getValue()));
	}
}
