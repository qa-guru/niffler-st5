package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.ui.page.StartPage;
import org.junit.jupiter.api.Test;

public class ProfileTest extends BaseWebTest {

    @Test
    @TestUser
    void updateNameTest(UserJson user) {
        String name = new Faker().name().firstName();
        Selenide.open(StartPage.URL, StartPage.class)
                .login(user)
                .openProfile()
                .setName(name)
                .checkMessage("Profile successfully updated")
                .checkName(name);
    }

    @Test
    @TestUser
    void changeCurrencyTest(UserJson user) {
        Selenide.open(StartPage.URL, StartPage.class)
                .login(user)
                .openProfile()
                .setCurrency(CurrencyValues.EUR)
                .checkMessage("Profile successfully updated")
                .checkCurrency(CurrencyValues.EUR);
    }

    @Test
    @TestUser
    void addCategoryTest(UserJson user) {
        String category = new Faker().beer().name();
        Selenide.open(StartPage.URL, StartPage.class)
                .login(user)
                .openProfile()
                .addCategory(category)
                .checkCategoryList(category);
    }

}