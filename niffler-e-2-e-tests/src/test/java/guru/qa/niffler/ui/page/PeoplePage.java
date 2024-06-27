package guru.qa.niffler.ui.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.constant.Friendship;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class PeoplePage extends BasePage<PeoplePage> {

    public static final String URL = CONFIG.frontUrl() + "/people";

    private final ElementsCollection tableRows = $("table.abstract-table").$("tbody").$$("tr");

    @Step("Проверить, что у пользователя {user.username} запрос дружбы в статусе {1}")
    public void assertThatFriendshipHasStatus(UserJson user, Friendship expectedStatus) {
        getRowWithUsername(user).shouldHave(text(expectedStatus.getMessage()));
    }

    @Step("Проверить, что пользователь {user.username} может принять запрос дружбы")
    public PeoplePage assertThatSubmitActionIsEnabled(UserJson user) {
        getRowWithUsername(user).$("button.button-icon_type_submit").shouldBe(visible, enabled);
        return this;
    }

    @Step("Проверить, что пользователь {user.username} может отклонить запрос дружбы")
    public void assertThatDeclineActionIsEnabled(UserJson user) {
        getRowWithUsername(user).$("button.button-icon_type_close").shouldBe(visible, enabled);
    }

    private SelenideElement getRowWithUsername(UserJson user) {
        return tableRows.find(text(user.username()));
    }

}