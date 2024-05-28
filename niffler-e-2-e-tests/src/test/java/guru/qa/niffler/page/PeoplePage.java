package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.constant.Friendship;
import guru.qa.niffler.model.UserJson;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class PeoplePage {

    private final ElementsCollection tableRows = $("table.abstract-table").$("tbody").$$("tr");

    public void assertThatFriendshipHasStatus(UserJson user, Friendship expectedStatus) {
        getRowWithUsername(user).shouldHave(text(expectedStatus.getMessage()));
    }

    public PeoplePage assertThatSubmitActionIsEnabled(UserJson user) {
        getRowWithUsername(user).$("button.button-icon_type_submit").shouldBe(visible, enabled);
        return this;
    }

    public void assertThatDeclineActionIsEnabled(UserJson user) {
        getRowWithUsername(user).$("button.button-icon_type_close").shouldBe(visible, enabled);
    }

    private SelenideElement getRowWithUsername(UserJson user) {
        return tableRows.find(text(user.username()));
    }

}