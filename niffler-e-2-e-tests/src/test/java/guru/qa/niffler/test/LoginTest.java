package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.data.repository.UserRepositorySpringJdbc;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.pages.AuthPage;
import guru.qa.niffler.pages.StartPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@WebTest
public class LoginTest {

    //   UserRepository userRepository = UserRepository.getInstance();

    UserRepository userRepository = new UserRepositorySpringJdbc();
    UserEntity userDataUser;

    @BeforeEach
    void createUserForTest() {

        UserAuthEntity user = new UserAuthEntity();
        user.setUsername("jdbc_user12132");
        user.setPassword("aA!123456");
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setEnabled(true);
        user.setCredentialsNonExpired(true);
        userRepository.createUserInAuth(user);

        UserEntity userEntity = new UserEntity();
        userDataUser.setUsername("jdbc_user12132");
        userDataUser.setCurrency(CurrencyValues.RUB);
        userDataUser = userRepository.createUserInUserdata(userDataUser);
    }


    @Test
    void loginTest() {
        StartPage startPage = new StartPage();
        AuthPage authPage = new AuthPage();

        Selenide.open("http://127.0.0.1:3000/");
        startPage.clickLoginButton();
        // authPage.login(user.username(), user.testData().password());
        authPage.login("jdbc_user12132", "aA!123456");
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").should(visible);

        userRepository.findUserInUserdataById(userDataUser.getId());
    }
}
