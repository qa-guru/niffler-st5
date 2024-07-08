package guru.qa.niffler.test.user;

import com.github.javafaker.Faker;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.*;
import guru.qa.niffler.jupiter.annotation.DbUser;
import guru.qa.niffler.jupiter.extension.DbCreateUserExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DbCreateUserExtension.class)
public class UserTest extends BaseWebTest {

    UserRepository userJdbcRepo = new UserRepositoryJdbc();
    UserRepositorySpringJdbc userSpringJdbcRepo = new UserRepositorySpringJdbc();

    SpendRepositoryJdbc spendJdbcRepo = new SpendRepositoryJdbc();
    SpendRepositorySpringJdbc spendSpringJdbcRepo = new SpendRepositorySpringJdbc();

    Faker faker = new Faker();

    @Test
    @DbUser
    void loginTest(UserJson userJson) {
        open(CFG.frontUrl());
        welcomePage.goToLogin();
        loginPage.login(userJson.username(), userJson.testData().password());
        assertTrue(menu.isAvatarVisible(), "Пользователь не вошёл в систему");
    }

    @Test
    @DbUser
    void updateUserInAuthTest(UserJson userJson) {
        open(CFG.frontUrl());
        welcomePage.goToLogin();
        loginPage.login(userJson.username(), userJson.testData().password());

        assertTrue(menu.isAvatarVisible(), "Пользователь не вошёл в систему");

        //тк не было задания на какие-любо тесты, то вызываю методы просто для дебага, посмотреть работу
        UserEntity user = userSpringJdbcRepo.getUserInUserDataByName(userJson.username()).get();
        UUID id = user.getId();

        userSpringJdbcRepo.findUserInUserDataById(id);
        userSpringJdbcRepo.updateUserInAuth(userSpringJdbcRepo.getUserInUserAuthByName(userJson.username()).get());
        userSpringJdbcRepo.updateUserInUserdata(user);

        userJdbcRepo.findUserInUserDataById(id);
        userJdbcRepo.updateUserInAuth(userSpringJdbcRepo.getUserInUserAuthByName(userJson.username()).get());
        userJdbcRepo.updateUserInUserdata(user);

        createSpend(user.getUsername()); // раз
        createSpend(user.getUsername()); // два

        assertEquals(spendSpringJdbcRepo.findAllSpendsByUsername(user.getUsername()).size(), 2);
        assertEquals(spendJdbcRepo.findAllSpendsByUsername(user.getUsername()).size(), 2);

        menu.logout();
        welcomePage.goToLogin();
        loginPage.login(userJson.username(), userJson.testData().password());
        int i = 0;//todo - пусто в база авторизации в табличке пользователь, продебажить
    }

    private void createSpend(String userName) {
        CategoryEntity category = new CategoryEntity();
        category.setId(UUID.randomUUID());
        category.setCategory("TestCategory_" + UUID.randomUUID());
        category.setUsername(userName);

        SpendEntity spend = new SpendEntity();
        spend.setId(UUID.randomUUID());
        spend.setUsername(userName);
        spend.setCurrency(CurrencyValues.RUB);
        spend.setSpendDate(faker.date().birthday());
        spend.setAmount(100.000);
        spend.setDescription(faker.animal().name());
        spend.setCategory(category);

        spendSpringJdbcRepo.createCategory(category);
        spendSpringJdbcRepo.createSpend(spend);
    }
}
