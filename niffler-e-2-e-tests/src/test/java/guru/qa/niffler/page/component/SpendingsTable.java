package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import java.util.List;

public class SpendingsTable extends BaseComponent<SpendingsTable> {
    /**
     * Конструктор для создания экземпляра компонента.
     *
     * @param self Селектор для элемента компонента
     */
    public SpendingsTable(SelenideElement self) {
        super(self);
    }

    public List<String> getAllSpendings() {
        return getSelf().$$("tr").texts();
    }
}
