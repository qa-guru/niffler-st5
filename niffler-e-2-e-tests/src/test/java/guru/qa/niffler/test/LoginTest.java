package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.ui.page.StartPage;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class LoginTest extends BaseWebTest {

    @Test
    void calendarTest(@User UserJson user) {
        Selenide.open(StartPage.URL, StartPage.class)
                .login(user)
                .setDate(new Date());
    }

}