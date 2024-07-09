package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class ReactCalendar extends BaseComponent {
    public ReactCalendar(SelenideElement self) {
        super(self);
    }

    public ReactCalendar() {
        super($(".calendar-wrapper"));
    }

    private final SelenideElement calendarInput = self.$(".react-datepicker__input-container input");

    public ReactCalendar selectDate(String date) {
        calendarInput
                .sendKeys(Keys.CONTROL+"a");
        calendarInput
                .sendKeys(Keys.DELETE);
        calendarInput
                .setValue(date)
                .pressEnter();
        return this;
    }
    public ReactCalendar checkDate(String date) {
        calendarInput.getText().equals(date);
        return this;
    }
}
