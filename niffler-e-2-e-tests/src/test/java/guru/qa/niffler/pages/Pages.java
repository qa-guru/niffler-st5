package guru.qa.niffler.pages;

import static com.codeborne.selenide.Selenide.page;

public class Pages {

    private WelcomePage welcomePage;
    private LoginPage loginPage;

    public WelcomePage mainPage() {
        return welcomePage = (welcomePage == null ? page(WelcomePage.class) : welcomePage);
    }

    public LoginPage loginPage() {
        return loginPage = (loginPage == null ? page(LoginPage.class) : loginPage);
    }
}
