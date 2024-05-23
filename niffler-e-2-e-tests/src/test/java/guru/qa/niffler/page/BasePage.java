package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends BasePage<?>> {

    protected static final Config CFG = Config.getInstance();

    private final SelenideElement toaster = $(".Toastify__toast");

    public abstract T checkPageLoaded();

    @SuppressWarnings("unchecked")
    public T checkMessage(String expectedText) {
        toaster.should(text(expectedText));
        return (T) this;
    }
}
