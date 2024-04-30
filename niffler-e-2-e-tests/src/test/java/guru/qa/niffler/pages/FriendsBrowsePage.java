package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class FriendsBrowsePage {

    private final SelenideElement peopleContentTableWrapper = $(".people-content");
    private final ElementsCollection friends = $(".table tbody").$$("tr");
    private final SelenideElement submitInvitationBtn = $("div[data-tooltip-id='submit-invitation']");
    private final SelenideElement declineInvitationBtn = $("div[data-tooltip-id='decline-invitation']");

    /**
     * Проверка видимости строки в таблице друзей по 'статусу дружбы'
     */
    public boolean checkRowWithStatus(String status) {
        pageLoaded();
        return friends.find(text(status)).exists();
    }

    /**
     * Проверка видимости кнопок отклонить/подтвердить в таблице друзей
     */
    public boolean checkInvitations() {
        pageLoaded();
        return submitInvitationBtn.exists() && declineInvitationBtn.exists();
    }

    private void pageLoaded() {
        peopleContentTableWrapper.should(visible);
    }
}