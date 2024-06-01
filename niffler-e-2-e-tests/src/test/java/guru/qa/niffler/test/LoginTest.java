package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.data.repository.UserRepositoryHibernate;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@WebTest
public class LoginTest {

    UserRepository userRepository = new UserRepositoryHibernate();
    UserEntity userDataUser;
    String userName = Faker.instance().name().name();
    UserAuthEntity user;
    UserEntity userEntity;


    @TestUser()
    @Test
    void loginTest(UserJson userJson) {
        Selenide.open("http://127.0.0.1:3000/");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userJson.username());
        $("input[name='password']").setValue(userJson.testData().password());
        $("button[type='submit']").click();
        $(".header__avatar").should(visible);

        user = new UserAuthEntity();
        user.setUsername(userName);
        user.setPassword("12345");
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        userRepository.createUserInAuth(user);
    }
}