package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class PeoplePage {
    final ElementsCollection
            rows = $(".abstract-table tbody").
            $$("tr");

    public SelenideElement userNameRow (String username) {
        return rows.find((text(username)));
    }

    public boolean isFriend (String username){
        userNameRow(username).lastChild().
                $("div").
                shouldHave(text("You are friends"));
        return true;
    }

    public boolean isInviteSent (String username){
        userNameRow(username).lastChild().
                $("div").
                shouldHave(text("Pending invitation"));
        return true;
    }

    public boolean isInvitationReceived(String username) {
        userNameRow(username).lastChild().
                $(".abstract-table__buttons div").
                shouldHave(attribute(
                        "data-tooltip-id",
                        "submit-invitation"));
        return true;
    }
}
