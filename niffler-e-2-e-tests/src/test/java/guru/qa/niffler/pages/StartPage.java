package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class StartPage {

    private final SelenideElement header = $(".main__header");
    private final SelenideElement loginButton =$("a[href*='redirect']");
    private final SelenideElement registerButton = $("a[href*='register']");

    public void clickLoginButton() {

        loginButton.click();
    }

    public StartPage clickRegisterButton() {

        registerButton.click();
        return this;
    }

    public void checkHeaderText(String expectedText) {
        header.shouldHave(text(expectedText));
    }
}
