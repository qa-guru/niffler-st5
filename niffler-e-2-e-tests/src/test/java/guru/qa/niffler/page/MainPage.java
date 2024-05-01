package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.attributes;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {
	private final ElementsCollection listSpending =  $$x("//tbody/tr");
	private final SelenideElement deleteSelectButton = $x("//button[contains(text(),'Delete selected')]");

	public void selectSpending (String spending){
		listSpending.find(text(spending)).$$("td").first().scrollTo().click();
	}
	public void clickDeleteSelectButton(){
		deleteSelectButton.click();
	}
	public void checkListSpendingIsEmpty(){
		listSpending.shouldHave(size(0));
	}
}
