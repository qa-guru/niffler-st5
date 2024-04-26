package guru.qa.niffler.pageObjects;

import com.codeborne.selenide.Selenide;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage>{

  public LoginPage doLogin() {
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue("dima");
        $("input[name='password']").setValue("12345");
        $("button[type='submit']").click();
        return this;
    }

}
