package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Selenide.$$x;

public class ProfilePage {

	private final ElementsCollection categoriesList = $$x("//*[@class='categories__list']/li");

	@Step("Проверить, что {category} присутствует в списке категорий ")
	public ProfilePage checkCategoriesInList(String category) {
		categoriesList.shouldHave(texts(category));
		return this;
	}
}
