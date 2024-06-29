package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Step;

import java.util.List;

import static guru.qa.niffler.condition.SpendsCondition.spendsInTable;

public class SpendingsTable extends BaseComponent<SpendingsTable> {
    /**
     * Конструктор для создания экземпляра компонента.
     *
     * @param self Селектор для элемента компонента
     */
    public SpendingsTable(SelenideElement self) {
        super(self);
    }

    @Step("Получить список (descriptions) всех трат")
    public List<String> getAllSpendings() {
        return getSelf().$$("tr").texts();
    }

    @Step("Проверка: таблица 'Spendings' содержит ожидаемые траты")
    public void shouldHaveSpendings(SpendJson... expectedSpends) {
        getSelf().$$("tr").shouldHave(spendsInTable(expectedSpends));
    }
}
