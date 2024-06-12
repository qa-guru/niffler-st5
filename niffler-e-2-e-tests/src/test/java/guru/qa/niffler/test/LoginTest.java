package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.user.DbCreateUserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.UiBot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith({
        BrowserExtension.class,
        DbCreateUserExtension.class})
public class LoginTest {

    private final UiBot ui = new UiBot();

    @Test
    @TestUser
    void loginTest(UserJson user) {
        Selenide.open("http://127.0.0.1:3000/main");
        ui.startPage().login(user);
    }

}