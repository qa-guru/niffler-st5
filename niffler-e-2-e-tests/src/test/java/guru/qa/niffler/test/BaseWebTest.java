package guru.qa.niffler.test;

import guru.qa.niffler.api.GatewayApiClient;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.*;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.page.*;
import guru.qa.niffler.page.component.Header;
import org.junit.jupiter.api.BeforeAll;

import java.util.Arrays;

public class BaseWebTest {

    protected static final Config CFG = Config.getInstance();

    protected static final MainPage mainPage = new MainPage();
    protected static final WelcomePage welcomePage = new WelcomePage();
    protected static final LoginPage loginPage = new LoginPage();
    protected static final Header menu = new Header();
    protected static final FriendsBrowsePage friendsPage = new FriendsBrowsePage();
    protected static final PeopleBrowsePage peoplePage = new PeopleBrowsePage();
    protected static final ProfilePage profilePage = new ProfilePage();

    protected static final String[] usersTestLogins = {"duck", "barsik", "cat", "fish"};

    protected final SpendApiClient spendApiClient = new SpendApiClient();
    protected final GatewayApiClient gatewayApiClient = new GatewayApiClient();

    @BeforeAll
    public static void setupEnvironment() {
        System.setProperty("repo", "hibernate");
        UserRepository userRepository = UserRepository.getInstance();

//        Arrays.stream(usersTestLogins).forEach(user -> {
//            try {
//                userRepository.findUserInUserData(user);
//                return;
//            } catch (Exception ignore) {
//            }
//
//            AuthorityEntity read = new AuthorityEntity();
//            read.setAuthority(Authority.read);
//            AuthorityEntity write = new AuthorityEntity();
//            write.setAuthority(Authority.write);
//
//            UserAuthEntity userToCommit = new UserAuthEntity();
//            userToCommit.setUsername(user);
//
//            userToCommit.setPassword("12345");
//            userToCommit.setEnabled(true);
//            userToCommit.setAccountNonExpired(true);
//            userToCommit.setAccountNonLocked(true);
//            userToCommit.setCredentialsNonExpired(true);
//            userToCommit.addAuthorities(read, write);
//
//            userRepository.createUserInAuth(userToCommit);
//
//            UserEntity userEntity = new UserEntity();
//            userEntity.setUsername((user));
//            userEntity.setFirstname((user + "Name"));
//            userEntity.setCurrency(CurrencyValues.RUB);
//            userRepository.createUserInUserData(userEntity);
//        });

    }
}
