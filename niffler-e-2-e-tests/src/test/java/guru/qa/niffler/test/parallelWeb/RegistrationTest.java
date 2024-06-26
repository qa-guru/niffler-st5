package guru.qa.niffler.test.parallelWeb;

import ch.qos.logback.core.testUtil.RandomUtil;
import guru.qa.niffler.jupiter.extension.DbCreateUserExtension;
import guru.qa.niffler.page.RegisterPage;
import guru.qa.niffler.test.BaseWebTest;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Isolated;

import static com.codeborne.selenide.Selenide.open;

@Epic("Регистрация пользователя")
@Tag("ParallelWebTests")
@Isolated
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(2)
@ExtendWith(DbCreateUserExtension.class)
public class RegistrationTest extends BaseWebTest {

    @Test
    @DisplayName("Регистрация пользователя в системе")
    void registrationTest() {

        String username = "user_" + RandomUtil.getPositiveInt();
        String password = "12345";

        open(RegisterPage.URL, RegisterPage.class)
                .fillRegisterPage(username, password, password)
                .successSubmit()
                .login(username, password)
                .waitForPageLoaded();
    }


}
