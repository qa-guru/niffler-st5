package guru.qa.niffler.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class LandingPage {

    private final SelenideElement header = $(".main__header");
    private final SelenideElement loginBtnSection = $(".main__links");
    private final SelenideElement loginLink = loginBtnSection.$(byText("Login"));
    private final SelenideElement registerLink = loginBtnSection.$(byText("Register"));

    // Method to open the landing page
    public LandingPage open() {
        Selenide.open("/"); // Adjust the URL as needed, for example, the root URL of the site
        return this;
    }

    // Method to ensure the page is loaded
    public void checkIsLoaded() {
        header.shouldBe(visible);
    }

    // Method to navigate to the login page
    public void clickLogin() {
        loginLink.click();
    }

    // Method to navigate to the registration page
    public void clickRegister() {
        registerLink.click();
    }
}
