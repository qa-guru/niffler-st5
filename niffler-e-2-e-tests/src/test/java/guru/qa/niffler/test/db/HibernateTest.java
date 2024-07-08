package guru.qa.niffler.test.db;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.DbUser;
import guru.qa.niffler.jupiter.annotation.meta.JdbcTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.WelcomePage;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
public class HibernateTest {

    private final WelcomePage welcomePage = new WelcomePage();
    private final LoginPage loginPage = new LoginPage();
    private final Header menu = new Header();

    UserRepository userRepository = UserRepository.getInstance();
    SpendRepository spendRepository = SpendRepository.getInstance();

    private final String CATEGORY = "Обучение Advanced 5";

    @BeforeAll
    public static void setupEnvironment() {
        System.setProperty("repo", "hibernate");
    }

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000");
        welcomePage.goToLogin();
    }

    @Category(
            category = CATEGORY,
            username = "dima"
    )
    @Spend(
            spendDate = "2024-04-18",
            category = CATEGORY,
            currency = CurrencyValues.RUB,
            amount = 79000.00,
            description = "testDescription",
            username = "dima"
    )
    @Description("Перед тестом создаётся категория и трата, после теста удаляются")
    @DisplayName("Создание Spend через запрос в БД (для дз 'Hibernate')")
    @Test()
    void spendTest(SpendJson spendJson) {
        loginPage.login("dima", "12345");

        assertNotNull(spendRepository.findCategory(spendJson.category(), spendJson.username()));
        assertNotNull(spendRepository.findAllSpendsByUsername(spendJson.username()));
    }

    @DisplayName("Создание User через запрос в БД (для дз 'Hibernate')")
    @DbUser
    @Test
    void userTest(UserJson userJson) {
        loginPage.login(userJson.username(), userJson.testData().password());

        assertTrue(menu.isAvatarVisible(), "Пользователь не вошёл в систему");

        assertNotNull(userRepository.findUserInAuth(userJson.username()));
        assertNotNull(userRepository.findUserInUserData(userJson.username()));
    }

}
