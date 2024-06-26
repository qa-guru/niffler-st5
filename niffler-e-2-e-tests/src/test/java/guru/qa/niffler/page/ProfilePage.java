package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage extends BasePage<ProfilePage> {

    public static final String URL = CFG.frontUrl() + "profile";

    private final SelenideElement userName = $(".avatar-container figcaption");
    private final SelenideElement nameInput = $("input[name='firstname']");
    private final SelenideElement surnameInput = $("input[name='surname']");
    private final SelenideElement profileAvatar = $(".profile__avatar");
    private final SelenideElement editAvatar = $(".edit-avatar__container");
    private final SelenideElement avatarInput = $("input[type='file']");
    private final SelenideElement categoryInput = $(".form__input[name='category']");
    private final SelenideElement createButton = $(byText("Create"));
    private final ElementsCollection categoriesList = $$(".categories__item");
    private final SelenideElement submitButton = $("button[type='submit']");

    @Step("Check that page is loaded")
    @Override
    public ProfilePage waitForPageLoaded() {
        userName.should(visible);
        return this;
    }

    @Step("Установить 'Имя': {0}")
    public ProfilePage setName(String name) {
        nameInput.sendKeys(Keys.CONTROL, "A");
        nameInput.append(name);
        return this;
    }

    @Step("Установить 'Фамилия': {0}")
    public ProfilePage setSurname(String surname) {
        surnameInput.sendKeys(Keys.CONTROL, "A");
        surnameInput.append(surname);
        return this;
    }

    @Step("Проверка: проверить имя пользователя {0}")
    public ProfilePage checkName(String name) {
        nameInput.shouldHave(value(name));
        return this;
    }

    @Step("Проверить фамилию пользователя {0}")
    public ProfilePage checkSurname(String surname) {
        surnameInput.shouldHave(value(surname));
        return this;
    }

    @Step("Загрузить изображение профиля - {0}")
    public String uploadAvatar(String filename) {
        editAvatar.click();
        avatarInput.uploadFromClasspath(filename);
        return profileAvatar.attr("src");
    }

    @Step("Создать категорию - {0}")
    public ProfilePage createCategory(String name) {
        categoryInput.append(name);
        createButton.click();
        return this;
    }

    @Step("Проверка: в списке категорий - {0}")
    public void shouldHaveCategories(String... categories) {
        for (String category : categories) {
            categoriesList.findBy(text(category)).shouldBe(visible);
        }
    }

    @Step("Сохранить изменения")
    public ProfilePage submit() {
        submitButton.click();
        return this;
    }
}
