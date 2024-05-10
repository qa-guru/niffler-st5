package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;

import io.qameta.allure.Step;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class AllPeopleListPage {

    private final ElementsCollection rowsTablePeople = $(".abstract-table tbody").$$("tr");

    //TODO нужно избежать дублирования кода
    @Step("Проверка отображения кнопок Принять или Отклонить приглашение")
    public void checkVisibleButtonsInviteReceived(String userName) {
    rowsTablePeople.find(text(userName)).$("div[data-tooltip-id='submit-invitation']").shouldHave(visible);
    rowsTablePeople.find(text(userName)).$("div[data-tooltip-id='decline-invitation']").shouldHave(visible);

    }

    @Step("Проверка отображения кнопки Удалить из друзей")
    public void checkVisibleButtonRemoveFriend(String userName) {
        rowsTablePeople.find(text(userName)). $("div[data-tooltip-id='remove-friend']").shouldHave(visible);
    }

    @Step("Проверка отображения кнопки Добавить в друзья")
    public void checkVisibleAddFriend(String userName) {
        rowsTablePeople.find(text(userName)). $("div[data-tooltip-id='add-friend']").shouldHave(visible);
    }



}
