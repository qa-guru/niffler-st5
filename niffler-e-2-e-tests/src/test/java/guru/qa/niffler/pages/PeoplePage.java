package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class PeoplePage {

    private final ElementsCollection friendsRows =  $$("table tbody tr");



    public PeoplePage openPage() {

        Selenide.open("http://127.0.0.1:3000/people");
        return this;
    }

    public void checkStatus(String friendName, String status) {

        friendsRows.find(text(friendName)).$$("td").last()
                .shouldHave(text(status));
    }
}
