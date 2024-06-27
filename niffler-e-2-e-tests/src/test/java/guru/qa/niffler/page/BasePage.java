package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

/**
 * Абстрактный класс для страниц, который обеспечивает базовые функции для взаимодействия со страницами.
 */
public abstract class BasePage<T extends BasePage<?>> {

    /**
     * Возвращает экземпляр конфигурации.
     */
    protected static final Config CFG = Config.getInstance();

    /**
     * Селектор для элемента Toastify, который отображает уведомления.
     */
    private final SelenideElement tostifyAlert = $("div .Toastify__toast-body");

    /**
     * Абстрактный метод, который ожидает загрузку страницы.
     *
     * @return Экземпляр класса BasePage
     */
    public abstract T waitForPageLoaded();

    /**
     * Метод для проверки, что уведомление с текстом `expectedText` появилось на странице.
     *
     * @param expectedText Текст, который ожидается в уведомлении
     * @return Экземпляр класса BasePage
     */
    @Step("Check that success message appears: {expectedText}")
    @SuppressWarnings("unchecked")
    public T checkToasterMessage(String expectedText) {
        // Проверяем, что уведомление является видимым и содержит текст `expectedText`
        tostifyAlert.should(visible).should(text(expectedText));
        // Возвращаем экземпляр класса BasePage
        return (T) this;
    }
}
