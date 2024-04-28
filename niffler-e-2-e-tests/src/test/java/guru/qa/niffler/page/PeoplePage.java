package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class PeoplePage {
    final ElementsCollection
            rows = $(".abstract-table tbody").
            $$("tr");

    public SelenideElement userNameRow (String username) {
        return rows.find((text(username)));
    }
}
