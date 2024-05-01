package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class StartPage {

    private final SelenideElement header = $(".main__header");
    private final SelenideElement loginButton =$("a[href*='redirect']");
    private final SelenideElement registerButton = $("a[href*='register']");

    public StartPage clickLoginButton() {
        loginButton.click();
        return this;
    }

    public StartPage clickRegisterButton() {
        registerButton.click();
        return this;
    }

    public String getHeaderText() {

        return header.getText();
    }
}
