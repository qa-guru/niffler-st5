package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;


public class MainPage {

    private final ElementsCollection spendingRows = $(".spendings-table tbody")
            .$$("tr");

    private final SelenideElement deleteButton = $(".spendings__bulk-actions button");

    public MainPage openPage() {

        Selenide.open("http://127.0.0.1:3000/main");
        return this;
    }

    public MainPage clickCheckbox(String description) {

        spendingRows.findBy(text(description))
                .$$("td").first().scrollIntoView(true)
                .$("input[type='checkbox']").click();
        return this;
    }

    public MainPage deleteSpending() {

        deleteButton.click();
        return this;
    }

    public MainPage checkSpendingsCount(int count) {

        spendingRows.shouldHave(size(count));
        return this;
    }

    public MainPage checkSpendingsDeletedText() {

        $(".Toastify__toast-body").shouldHave(text("Spendings deleted"));
        return this;
    }


}

