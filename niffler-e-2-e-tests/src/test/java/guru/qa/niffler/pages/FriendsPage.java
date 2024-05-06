package guru.qa.niffler.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {

    private final SelenideElement friendsRows =  $("table tbody tr");
    private final SelenideElement tableMessage = $(".main-content__section div");
    private final SelenideElement removeFriendBottom = $("#remove-friend");



    public FriendsPage openPage() {

        Selenide.open("http://127.0.0.1:3000/friends");
        return this;
    }

    public void checkNoFriendsMessage() {

        tableMessage.shouldHave(text("There are no friends yet!"));
    }

    public void checkFriendsName(String friendName) {

        friendsRows.$$("td").get(1).shouldHave(text(friendName));
    }

    public FriendsPage checkOptionInvite(String nameOption) {

        friendsRows.$("div[data-tooltip-id*='"+nameOption+"-invitation']").shouldBe(visible);
        return this;
    }


}
