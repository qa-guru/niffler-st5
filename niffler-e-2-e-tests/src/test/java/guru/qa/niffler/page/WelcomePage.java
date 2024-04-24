package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class WelcomePage {
    private final SelenideElement loginRedirect = $("a[href*='redirect']"),
            registrationRedirect = $("a[href*='register']");

    public LoginPage doLoginRedirect(){
        loginRedirect.click();
        return new LoginPage();
    }
//    public RegistrationPage doRegistrationRedirect (){
//        registrationRedirect.click();
//        return new RegistrationPage();
//    }
    public WelcomePage waitPageLoaded(){
        loginRedirect.should(visible);
        registrationRedirect.should(visible);
        return this;
    }

}
