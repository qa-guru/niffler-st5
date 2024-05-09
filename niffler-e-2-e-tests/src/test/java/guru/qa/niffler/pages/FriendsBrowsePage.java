package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;

@Getter
public class FriendsBrowsePage {
    private final SelenideElement peopleContentTableWrapper = $(".people-content");
    private final ElementsCollection friends = $(".table tbody").$$("tr");
    private final SelenideElement submitInvitationBtn = $("div[data-tooltip-id='submit-invitation']");
    private final SelenideElement declineInvitationBtn = $("div[data-tooltip-id='decline-invitation']");
}