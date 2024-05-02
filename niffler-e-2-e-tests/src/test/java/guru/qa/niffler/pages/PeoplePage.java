package guru.qa.niffler.pages;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverConditions.url;

public class PeoplePage extends BasePage<PeoplePage> {
    public final String url = "http://127.0.0.1:3000/people";

    private final ElementsCollection rowsPeopleTable = $(".abstract-table tbody").$$("tr")
            .as("Строки таблицы с друзьями");
    private final SelenideElement peopleContent = $(".table abstract-table");

    @Override
    public PeoplePage waitForPageLoaded() {
        peopleContent.shouldBe(visible);
        return this;
    }

    @Override
    public PeoplePage checkPage() {
        Selenide.webdriver()
                .shouldHave(url(url));
        return this;
    }
    @Step("Проверить наличие друзей у пользователя с именем {0}")
    public void checkFriendIsPresent(String userName) {
        final String expectedText = "You are friends";
        getUserNameRow(userName).lastChild().
                $("div").
                shouldHave(text(expectedText));
    }

    @Step("Добавить в друзья пользователя с именем {0}")
    public PeoplePage addFriendIsInviteSent(String userName) {
        getUserNameRow(userName).lastChild().
                click();
        return this;
    }

    @Step("Проверить наличие отправленной заявки на дружбу для пользователя с именем {0}")
    public PeoplePage checkFriendIsInviteSent(String userName) {
        final String expectedText = "Pending invitation";
        getUserNameRow(userName).lastChild().
                $("div").
                shouldHave(text(expectedText));
        return this;
    }
    @Step("Проверить наличие заявки на согласие дружить, для пользователя с именем {0}")
    public PeoplePage checkFriendIsInvitationReceived(String userName) {
        SelenideElement userNameRow = getUserNameRow(userName).lastChild();
        userNameRow.$(".abstract-table__buttons div")
                .shouldHave(Condition.attribute(
                        "data-tooltip-id",
                        "submit-invitation"));
        return this;
    }

    private SelenideElement getUserNameRow(String username) {
        return rowsPeopleTable.find((text(username)));
    }
}
