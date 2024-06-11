package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.AuthorityEntity;
import guru.qa.niffler.data.entity.CurrencyValues;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.data.repository.UserRepositoryHibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginTest extends BaseWebTest {

    UserRepository userRepository = new UserRepositoryHibernate();
    UserEntity userDataUser;

    @BeforeEach
    void createUserForTest() {
        AuthorityEntity read = new AuthorityEntity();
        read.setAuthority(Authority.read);
        AuthorityEntity write = new AuthorityEntity();
        write.setAuthority(Authority.write);


        UserAuthEntity user = new UserAuthEntity();
        user.setUsername("jdbc_user8");
        user.setPassword("12345");
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.addAuthorities(
                read, write
        );

        userRepository.createUserInAuth(user);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("jdbc_user8");
        userEntity.setCurrency(CurrencyValues.RUB);
        userDataUser = userRepository.createUserInUserdata(userEntity);
    }

    @Test
    void loginTest() {
        Selenide.open(CFG.frontUrl());
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue("jdbc_user8");
        $("input[name='password']").setValue("12345");
        $("button[type='submit']").click();
        $(".header__avatar").should(visible);

        userRepository.findUserInUserdataById(userDataUser.getId());
    }

}
