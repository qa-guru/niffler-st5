package guru.qa.niffler.condition.user;

import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.UserJson;

public class UsersCondition {

    public static WebElementsCondition usersInTable(UserJson... expectedUsers) {
        return new UsersInTableCondition(expectedUsers);
    }

}
