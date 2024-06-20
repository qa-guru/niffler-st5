package guru.qa.niffler.test.user;

import guru.qa.niffler.data.entity.*;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.data.repository.UserRepositoryHibernate;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class LoginTest extends BaseWebTest {

    // по умолчанию UserRepositoryJdbc до тех пор пока не добавили системные переменные
    // UserRepository userRepository = UserRepository.getInstance();

    UserRepository userRepository = new UserRepositoryHibernate();

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
        open(CFG.frontUrl());
        welcomePage.goToLogin();
        loginPage.login(userLogin, "12345");

        assertTrue(menu.isAvatarVisible(), "Пользователь не вошёл в систему");
        assertNotNull(userRepository.findUserInUserDataById(userDataUser.getId()));
    }

}
