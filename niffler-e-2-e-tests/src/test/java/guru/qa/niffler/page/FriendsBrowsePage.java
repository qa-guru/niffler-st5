package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.PeopleTable;
import io.qameta.allure.Step;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;

@Getter
public class FriendsBrowsePage extends BasePage<FriendsBrowsePage>{

    public static final String URL = CFG.frontUrl() + "friends";

    private final SelenideElement peopleContentTableWrapper = $(".people-content");

    @Getter
    private final PeopleTable peopleTable = new PeopleTable($(".table"));

    @Step("Ожидание загрузки страницы")
    @Override
    public FriendsBrowsePage waitForPageLoaded() {
        peopleContentTableWrapper.shouldBe(Condition.visible);
        return this;
    }

}