package guru.qa.niffler.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class StartPage extends BasePage<StartPage> {

    public static final String URL = CFG.frontUrl();

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

    @Override
    public StartPage checkPageLoaded() {
        return null;
    }

    public StartPage openPage() {
        Selenide.open(URL);
        return this;
    }
}
