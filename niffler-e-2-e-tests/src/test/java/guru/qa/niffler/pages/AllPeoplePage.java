package guru.qa.niffler.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;


public class AllPeoplePage {
    private final ElementsCollection peopleTable = $$(".main-content__section tbody tr");

    @Step("Verifying whether the status is Pending invitation")
    public void verifyPendingInvitationStatusFrom(String username) {
        peopleTable.find(text(username)).$(".abstract-table__buttons div").shouldHave(text("Pending invitation"));
    }

    @Step("Verifying whether the status is You are friends")
    public AllPeoplePage verifyYAFriendsStatusWith(String username) {
        peopleTable.find(text(username)).$(".abstract-table__buttons div").shouldHave(text("You are friends"));
        return this;
    }

    @Step("Verifying if there is a Submit button")
    public AllPeoplePage verifyASubmitButtonFrom(String username) {
        peopleTable.find(text(username)).$(".abstract-table__buttons div").should(Condition.attribute("data-tooltip-content", "Submit invitation"));
        return this;
    }

    @Step("Verifying if there is a Decline button")
    public void verifyADeclineButtonFrom(String username) {
        peopleTable.find(text(username)).$(".abstract-table__buttons div").should(Condition.attribute("data-tooltip-content", "Decline invitation"));
    }
}
