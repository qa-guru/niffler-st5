package guru.qa.niffler.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.WebDriverConditions.url;


public class WelcomePage extends BasePage<WelcomePage>{

    public final String url = "http://127.0.0.1:3000/";
    private final SelenideElement loginBtn = $("a[href*='redirect']").as("Кнопка 'Login'");

    @Override
    public WelcomePage waitForPageLoaded() {
        loginBtn.shouldBe(visible);
        return this;
    }

    @Override
    public WelcomePage checkPage() {
        Selenide.webdriver()
                .shouldHave(url(url));
        return this;
    }
    @Step("Перейти на страницу авторизации")
    public LoginPage openLoginPage() {
        loginBtn.click();
        return page(LoginPage.class);
    }
}
