package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BaseComponent;

public class Calendar extends BaseComponent<Calendar> {
    /**
     * Конструктор для создания экземпляра компонента.
     *
     * @param self Селектор для элемента компонента
     */
    public Calendar(SelenideElement self) {
        super(self);
    }

}
