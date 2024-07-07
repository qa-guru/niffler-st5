package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class ReactCalendar extends BaseComponent {
    public ReactCalendar(SelenideElement self) {
        super(self);
    }

    public ReactCalendar() {
        super($(".react-datepicker"));
    }

    public ReactCalendar selectDate(String date) {
        self.$(".react-datepicker__input-container input").setValue(date);
        return this;
    }

}
