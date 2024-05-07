package guru.qa.niffler.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {
    private final ElementsCollection friendsTable = $$(".main-content__section tbody tr");

    @Step("Verifying whether the status is You are friends")
    public FriendsPage verifyYAFriendsStatusWith(String username) {
        friendsTable.find(text(username)).$(".abstract-table__buttons div").shouldHave(text("You are friends"));
        return this;
    }

    @Step("Verifying if there is a Submit button")
    public FriendsPage verifyASubmitButtonFrom(String username) {
        friendsTable.find(text(username)).$(".abstract-table__buttons div").should(Condition.attribute("data-tooltip-content", "Submit invitation"));
        return this;
    }

    @Step("Verifying if there is a Decline button")
    public void verifyADeclineButtonFrom(String username) {
        friendsTable.find(text(username)).$(".abstract-table__buttons div").should(Condition.attribute("data-tooltip-content", "Decline invitation"));
    }
}
