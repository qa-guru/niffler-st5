package guru.qa.niffler.condition.spends;

import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.SpendJson;

public class SpendsCondition {

    /**
     * Позволяет "перечитать" коллекцию элементов (трат), если с одним из них не прошла проверка.
     * Реализует механизм "умных ожиданий" Selenide для коллекции элементов.
     * @param expectedSpends массив трат
     * @return WebElementsCondition
     */
    public static WebElementsCondition spendsInTable(SpendJson... expectedSpends) {
        return new SpendsInTableCondition(expectedSpends);
    }
}