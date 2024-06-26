package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

/**
 * Абстрактный класс для компонентов страницы, который обеспечивает базовые функции для взаимодействия с компонентами.
 */
public abstract class BaseComponent<T extends BaseComponent<?>> {

    /**
     * Селектор для элемента компонента.
     */
    protected final SelenideElement self;

    /**
     * Конструктор для создания экземпляра компонента.
     * @param self Селектор для элемента компонента
     */
    public BaseComponent(SelenideElement self) {
        this.self = self;
    }

    public SelenideElement getSelf() {
        return self;
    }
}
