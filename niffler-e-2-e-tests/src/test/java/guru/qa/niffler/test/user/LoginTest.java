package guru.qa.niffler.test.user;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.data.entity.*;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.data.repository.UserRepositoryHibernate;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.WelcomePage;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class LoginTest extends BaseWebTest {

    // по умолчанию UserRepositoryJdbc до тех пор пока не добавили системные переменные
    // UserRepository userRepository = UserRepository.getInstance();

    UserRepository userRepository = new UserRepositoryHibernate();

    private final WelcomePage welcomePage = new WelcomePage();
    private final LoginPage loginPage = new LoginPage();
    private final Header menu = new Header();

    private final String userLogin = "user5155";
    private UserEntity userDataUser;

    @BeforeEach
    void createUserForTest() {
        AuthorityEntity read = new AuthorityEntity();
        read.setAuthority(Authority.read);
        AuthorityEntity write = new AuthorityEntity();
        write.setAuthority(Authority.write);

        UserAuthEntity user = new UserAuthEntity();
        user.setUsername(userLogin);
        user.setPassword("12345");
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.addAuthorities(read, write);

        userRepository.createUserInAuth(user);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userLogin);
        userEntity.setCurrency(CurrencyValues.RUB);
        userDataUser = userRepository.createUserInUserData(userEntity);
    }

    @Test
    void loginTest() {
        Selenide.open(CFG.frontUrl());
        welcomePage.goToLogin();
        loginPage.login(userLogin, "12345");

        assertTrue(menu.isAvatarVisible(), "Пользователь не вошёл в систему");
        assertNotNull(userRepository.findUserInUserDataById(userDataUser.getId()));
    }

}
