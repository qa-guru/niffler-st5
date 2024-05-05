package guru.qa.niffler.pages.auth;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class AuthPage {

    private final SelenideElement loginBtnSection = $(".main__links");
    private final SelenideElement loginBtn = loginBtnSection.$(byText("Login"));
    private final SelenideElement registerBtn = loginBtnSection.$(byText("Register"));

    public AuthPage openAuthPage() {
        open("http://127.0.0.1:3000/main");
        return this;
    }

    public void loginBtnClick() {
        loginBtn.click();
    }

    public void registerBtnClick() {
        registerBtn.click();
    }

}
