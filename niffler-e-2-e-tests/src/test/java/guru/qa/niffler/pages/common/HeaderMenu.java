package guru.qa.niffler.pages.common;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@Getter
public class HeaderMenu {
    private final SelenideElement friends = $("li[data-tooltip-id='friends']");
    private final SelenideElement people = $("li[data-tooltip-id='people']");
    private final SelenideElement logout = $("div[data-tooltip-id='logout']");
    private final SelenideElement avatar = $(".header__avatar");

    public boolean isAvatarVisible(){
        return avatar.is(visible);
    }
}