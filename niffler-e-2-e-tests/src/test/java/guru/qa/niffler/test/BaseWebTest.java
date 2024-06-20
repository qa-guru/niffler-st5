package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import guru.qa.niffler.page.common.HeaderMenu;

@WebTest
public class BaseWebTest {

    protected static final Config CFG = Config.getInstance();

    protected static final MainPage mainPage = new MainPage();
    protected static final WelcomePage welcomePage = new WelcomePage();
    protected static final LoginPage loginPage = new LoginPage();
    protected static final HeaderMenu menu = new HeaderMenu();
}
