package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;

@Getter
public class FriendsBrowsePage extends BasePage<FriendsBrowsePage>{

    public static final String URL = CFG.frontUrl() + "friends";

    private final SelenideElement peopleContentTableWrapper = $(".people-content");
    private final ElementsCollection friends = $(".table tbody").$$("tr");
    private final SelenideElement submitInvitationBtn = $("div[data-tooltip-id='submit-invitation']");
    private final SelenideElement declineInvitationBtn = $("div[data-tooltip-id='decline-invitation']");

    @Step("Ожидание загрузки страницы")
    @Override
    public FriendsBrowsePage waitForPageLoaded() {
        peopleContentTableWrapper.shouldBe(Condition.visible);
        return this;
    }
}