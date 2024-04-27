package guru.qa.niffler.pageObjects;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BasePage<MainPage> {

    public static final String CENTER = "{block: \"center\"}";
    private final String trSet = "tr";
    private final String tdSet = "td";
    private final String deleteBtn = ".spendings__bulk-actions button";
    private final String tableBody = ".spendings-table tbody";


    public MainPage selectCategory(String description) {
        SelenideElement rowWithSpending = $(tableBody)
                .$$(trSet)
                .find(text(description));
        rowWithSpending.$$(tdSet)
                .first()
                .scrollIntoView(CENTER)
                .click();
        return this;
    }

    public MainPage deleteCategory() {
        $(deleteBtn).click();
        $(tableBody).$$(trSet)
                .shouldHave(size(0));
        return this;
    }

}
