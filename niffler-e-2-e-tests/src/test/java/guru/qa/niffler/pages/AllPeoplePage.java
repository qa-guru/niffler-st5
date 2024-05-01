package guru.qa.niffler.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;


public class AllPeoplePage {
    private final ElementsCollection peopleTable = $$(".main-content__section tbody tr");

    @Step("Find people by username in table")
    public SelenideElement findFriendByUsername(String username) {
        return peopleTable.find(text(username));
    }

    @Step("Sending friend invitation")
    public AllPeoplePage sendingFriendInvitation(SelenideElement foundFriend) {
        foundFriend.$x("//div[@data-tooltip-id='add-friend']/button").click();
        return this;
    }

    @Step("Checking whether the status is Pending invitation")
    public void isStatusPendingInvitation(SelenideElement foundFriend) {
        foundFriend.$(".abstract-table__buttons div").getText().equals("Pending invitation");
    }

    @Step("Checking whether the status is You are friends")
    public AllPeoplePage isStatusYAFriends(SelenideElement foundFriend) {
        foundFriend.$(".abstract-table__buttons div").getText().equals("You are friends");
        return this;
    }

    @Step("Checking if there is a Submit button")
    public AllPeoplePage ifThereIsASubmitButton(SelenideElement foundFriend) {
        foundFriend.$$(".abstract-table__buttons div").first().should(Condition.attribute("data-tooltip-content", "Submit invitation"));
        return this;
    }

    @Step("Checking if there is a Decline button")
    public void ifThereIsADeclineButton(SelenideElement foundFriend) {
        foundFriend.$$(".abstract-table__buttons div").last().should(Condition.attribute("data-tooltip-content", "Decline invitation"));
    }
}
