package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.Getter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@Getter
public class Header extends BaseComponent<Header>{

    private final SelenideElement friends = self.$("li[data-tooltip-id='friends']");
    private final SelenideElement people = self.$("li[data-tooltip-id='people']");
    private final SelenideElement logout = self.$("div[data-tooltip-id='logout']");
    private final SelenideElement avatar = self.$(".header__avatar");

    public Header() {
        super($(".header"));
    }

    @Step("Проверка видимости аватара")
    public boolean isAvatarVisible() {
        return avatar.is(visible);
    }

    @Step("Выйти из системы")
    public void logout() {
        getLogout().click();
    }
}