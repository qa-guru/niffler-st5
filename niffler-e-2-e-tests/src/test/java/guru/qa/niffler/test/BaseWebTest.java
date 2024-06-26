package guru.qa.niffler.test;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.*;
import guru.qa.niffler.page.component.Header;
import org.junit.jupiter.api.BeforeAll;

@WebTest
public class BaseWebTest {

    protected static final Config CFG = Config.getInstance();

    protected static final MainPage mainPage = new MainPage();
    protected static final WelcomePage welcomePage = new WelcomePage();
    protected static final LoginPage loginPage = new LoginPage();
    protected static final Header menu = new Header();
    protected static final FriendsBrowsePage friendsPage = new FriendsBrowsePage();
    protected static final PeopleBrowsePage peoplePage = new PeopleBrowsePage();
    protected static final ProfilePage profilePage = new ProfilePage();

    protected final SpendApiClient spendApiClient = new SpendApiClient();

    @BeforeAll
    public static void setupEnvironment() {
        System.setProperty("repo", "hibernate");
    }
}
