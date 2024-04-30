package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.NoSuchElementException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class PeopleBrowsePage {

    private final SelenideElement peopleContentTableWrapper = $(".people-content");
    private final ElementsCollection friends = $(".table tbody").$$("tr");
    private final SelenideElement submitInvitationBtn = $("div[data-tooltip-id='submit-invitation']");
    private final SelenideElement declineInvitationBtn = $("div[data-tooltip-id='decline-invitation']");

    /**
     * Проверка видимости строки в таблице друзей по статусу
     */
    public boolean checkRowWithStatus(String status) {
        pageLoaded();

        try {
            friends
                    .find(text(status))
                    .$(".abstract-table__buttons")
                    .scrollIntoView(true);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Проверка видимости кнопки подтвердить в таблице пользователей
     */
    public boolean checkInvitations() {
        pageLoaded();

        try {
            submitInvitationBtn.scrollIntoView(true);
            declineInvitationBtn.should(visible);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private void pageLoaded() {
        peopleContentTableWrapper.should(visible);
    }
}