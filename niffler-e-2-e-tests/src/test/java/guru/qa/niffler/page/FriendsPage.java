package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.PeopleTable;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage extends BasePage<FriendsPage> {

    public static final String URL = CFG.frontUrl() + "friends";

    private final SelenideElement tableContainer = $(".people-content");
    private final PeopleTable table = new PeopleTable($(".table"));

    @Step("Check that the page is loaded")
    @Override
    public FriendsPage waitForPageLoaded() {
        tableContainer.shouldBe(Condition.visible);
        return this;
    }

    @Step("Check that friends count is equal to {expectedCount}")
    public FriendsPage checkExistingFriendsCount(int expectedCount) {
        table.getAllRows().shouldHave(size(expectedCount));
        return this;
    }

    @Step("Delete user from friends: {username}")
    public FriendsPage removeFriend(String username) {
        SelenideElement friendRow = table.getRowByUsername(username);
        SelenideElement actionsCell = table.getActionsCell(friendRow);
        actionsCell.$(".button-icon_type_close")
                .click();
        return this;
    }

    @Step("Accept invitation from user: {username}")
    public FriendsPage acceptFriendInvitationFromUser(String username) {
        SelenideElement friendRow = table.getRowByUsername(username);
        SelenideElement actionsCell = table.getActionsCell(friendRow);
        actionsCell.$(".button-icon_type_submit")
                .click();
        return this;
    }
}
