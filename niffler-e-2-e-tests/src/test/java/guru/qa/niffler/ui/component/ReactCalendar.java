package guru.qa.niffler.ui.component;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class ReactCalendar extends BaseComponent<ReactCalendar> {

    public ReactCalendar() {
        super($(".calendar-wrapper"));
    }

    public ReactCalendar(SelenideElement self) {
        super(self);
    }

    public ReactCalendar set(String date) {
        SelenideElement calendarInput = self.$("input");
        calendarInput.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), Keys.DELETE);
        calendarInput.sendKeys(date);
        return this;
    }


}
