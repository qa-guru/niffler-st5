package guru.qa.niffler.pageObjects;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {

    private String loginBtn = "a[href*='redirect']";
    private String userNameField = "input[name='username']";
    private String passwordField = "input[name='password']";
    private String submitBtn = "button[type='submit']";

    public LoginPage doLogin() {
        $(loginBtn).click();
        $(userNameField).setValue("dima");
        $(passwordField).setValue("12345");
        $(submitBtn).click();
        return this;
    }

}
