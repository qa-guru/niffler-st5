package guru.qa.niffler.page.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;
import lombok.Getter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.users.UsersCondition.usersInTable;

public class PeopleTable extends BaseComponent<PeopleTable> {

    private final ElementsCollection friends = $(".table tbody").$$("tr");
    @Getter
    private final SelenideElement submitInvitationBtn = $("div[data-tooltip-id='submit-invitation']");
    @Getter
    private final SelenideElement declineInvitationBtn = $("div[data-tooltip-id='decline-invitation']");

    /**
     * Конструктор для создания экземпляра компонента.
     *
     * @param self Селектор для элемента компонента
     */
    public PeopleTable(SelenideElement self) {
        super(self);
    }

    @Step("Получить все строки таблицы 'people-content'")
    public ElementsCollection getAllRows() {
        return friends;
    }

    public SelenideElement getRowByUsername(String username) {
        ElementsCollection allRows = getAllRows();
        SelenideElement table = $(".table");
        table.shouldBe(Condition.visible);
        return allRows.find(text(username));
    }

    public SelenideElement getUsernameCell(SelenideElement row) {
        return row.$$("td").get(1);
    }

    public SelenideElement getActionsCell(SelenideElement row) {
        return row.$$("td").get(3);
    }

    @Step("Проверка: таблица 'People' содержит ожидаемый список пользователей")
    public void shouldHavePeople(UserJson... expectedUsers) {
        getSelf().$$("tr").shouldHave(usersInTable(expectedUsers));
    }

}
