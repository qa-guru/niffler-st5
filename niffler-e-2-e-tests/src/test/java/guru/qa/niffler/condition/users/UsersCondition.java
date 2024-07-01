package guru.qa.niffler.condition.users;

import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.UserJson;

public class UsersCondition {

    /**
     * Позволяет "перечитать" коллекцию элементов (трат), если с одним из них не прошла проверка.
     * Реализует механизм "умных ожиданий" Selenide для коллекции элементов.
     *
     * @param expectedUsers массив трат
     * @return WebElementsCondition
     */
    public static WebElementsCondition usersInTable(UserJson... expectedUsers) {
        return new UsersInTableCondition(expectedUsers);
    }

}
