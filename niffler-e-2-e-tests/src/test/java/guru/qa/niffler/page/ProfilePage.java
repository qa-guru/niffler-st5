package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage {

	private final ElementsCollection categoriesList = $$x("//*[@class='categories__list']/li");

	private final SelenideElement firstnameInput = $x("//input[@name='firstname']"),
			usernameInput = $x("//*[@class='avatar-container']/figcaption"),
			surnameInput = $x("//input[@name='surname']"),
			currencyInput = $x("//*[@class='select-wrapper']");


	@Step("Проверить, что {category} присутствует в списке категорий ")
	public ProfilePage checkCategoriesInList(String category) {
		categoriesList.shouldHave(texts(category));
		return this;
	}

	@Step("Проверить логин пользователя ")
	public ProfilePage checkUsername(String username) {
		usernameInput.shouldHave(text(username));
		return this;
	}

	@Step("Проверить имя пользователя ")
	public ProfilePage checkFirstname(String firstname) {
		firstnameInput.shouldHave(attribute("value", firstname));
		return this;
	}

	@Step("Проверить фамилию пользователя ")
	public ProfilePage checkSurname(String surname) {
		surnameInput.shouldHave(attribute("value", surname));
		return this;
	}

	@Step("Проверить валюту пользователя ")
	public ProfilePage checkCurrency(String currency) {
		currencyInput.shouldHave(text(currency));
		return this;
	}
}
