package guru.qa.niffler.ui.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends BasePage<?>> {

    protected static final Config CONFIG = Config.getInstance();

    private final SelenideElement toaster = $(".Toastify__toast");

    @SuppressWarnings("unchecked")
    @Step("Проверить, что уведомление содержит текст \"{0}\"")
    public T checkMessage(String expectedText) {
        toaster.should(text(expectedText));
        return (T) this;
    }

}
