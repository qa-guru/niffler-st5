package guru.qa.niffler.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {
    private final ElementsCollection friendsTable = $$(".main-content__section tbody tr");

    @Step("Find people by username in table")
    public SelenideElement findFriendByUsername(String username) {
        return friendsTable.find(text(username));
    }

    @Step("Checking whether the status is You are friends")
    public FriendsPage isStatusYAFriends(SelenideElement foundFriend) {
        foundFriend.$(".abstract-table__buttons div").getText().equals("You are friends");
        return this;
    }

    @Step("Checking if there is a Submit button")
    public FriendsPage ifThereIsASubmitButton(SelenideElement foundFriend) {
        foundFriend.$$(".abstract-table__buttons div").first().should(Condition.attribute("data-tooltip-content", "Submit invitation"));
        return this;
    }

    @Step("Checking if there is a Decline button")
    public void ifThereIsADeclineButton(SelenideElement foundFriend) {
        foundFriend.$$(".abstract-table__buttons div").last().should(Condition.attribute("data-tooltip-content", "Decline invitation"));
    }
}
