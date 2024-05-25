package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.SpendRepositorySpringJdbc;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.data.repository.UserRepositoryJdbc;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@WebTest
public class LoginTest {

    UserRepository userRepository = new UserRepositoryJdbc();
    SpendRepository spendRepository = new SpendRepositorySpringJdbc();
    UserEntity userDataUser;
    String userName = Faker.instance().name().name();
    UserAuthEntity user;
    UserEntity userEntity;

    /*@BeforeEach
    void createUserForTest() {

        user = new UserAuthEntity();
        user.setUsername(userName);
        user.setPassword("12345");
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        userRepository.createUserInAuth(user);


        userEntity = new UserEntity();
        userEntity.setUsername(userName);
        userEntity.setCurrency(CurrencyValues.RUB);
        userDataUser = userRepository.createUserInUserdata(userEntity);
    }*/

    @TestUser()
    @Test
    void loginTest(UserJson userJson) {
        Selenide.open("http://127.0.0.1:3000/");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userJson.username());
        $("input[name='password']").setValue(userJson.testData().password());
        $("button[type='submit']").click();
        $(".header__avatar").should(visible);

        userRepository.findUserInUserdataById(userJson.id());
        spendRepository.findAllByUsername(userJson.username());

        /*String newUserName = Faker.instance().name().name();
        String newPassword = "123456";
        user.setUsername(newUserName);
        user.setPassword(newPassword);
        userEntity.setUsername(newUserName);
        userRepository.updateUserInAuth(user);
        userRepository.createUserInUserdata(userEntity);

        $("div.header__logout").click();
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(newUserName);
        $("input[name='password']").setValue(newPassword);
        $("button[type='submit']").click();*/
    }

}