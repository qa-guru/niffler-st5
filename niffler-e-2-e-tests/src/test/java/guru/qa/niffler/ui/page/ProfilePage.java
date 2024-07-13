package guru.qa.niffler.ui.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.CurrencyValues;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage extends BasePage<ProfilePage> {

    public static final String URL = CONFIG.frontUrl() + "profile";

    private final SelenideElement firstnameInput = $("[name = 'firstname']");
    private final SelenideElement submitBtn = $("[type=submit]");
    private final SelenideElement dropdownActivator = $(".select-wrapper");
    private final ElementsCollection dropdownOptions = $$("[id*='react-select-7-option']");
    private final SelenideElement categoryInput = $("[name='category']");
    private final SelenideElement createBtn = $x("//button[text()='Create']");
    private final SelenideElement categoryList = $(".categories__list");

    @Step
    public void checkCategoryList(String category) {
        categoryList.shouldHave(Condition.text(category));
    }

    @Step("Добавить категорию \"{0}\"")
    public ProfilePage addCategory(String category) {
        categoryInput.sendKeys(category);
        createBtn.scrollTo().click();
        return this;
    }

    @Step("Установить валюту {0}")
    public ProfilePage setCurrency(CurrencyValues currency) {
        dropdownActivator.scrollTo().click();
        dropdownOptions.find(Condition.text(currency.name())).click();
        clickSubmit();
        return this;
    }

    @Step("Проверить, что установлена ожидаемая валюта {0}")
    public void checkCurrency(CurrencyValues currency) {
        dropdownActivator.shouldHave(Condition.text(currency.name()));
    }

    @Step("Ввести имя {0}")
    public ProfilePage setName(String newName) {
        firstnameInput.clear();
        firstnameInput.sendKeys(newName);
        clickSubmit();
        return this;
    }

    @Step("Проверить полe \"Имя\"")
    public void checkName(String expected) {
        firstnameInput.should(value(expected));
    }

    private void clickSubmit() {
        submitBtn.scrollTo().click();
    }

}
