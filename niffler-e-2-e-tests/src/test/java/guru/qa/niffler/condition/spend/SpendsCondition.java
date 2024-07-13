package guru.qa.niffler.condition.spend;

import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.SpendJson;

public class SpendsCondition {

    public static WebElementsCondition spendsInTable(SpendJson... expectedSpends) {
        return new SpendsInTableCondition(expectedSpends);
    }

}
