package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ReactCalendar extends BaseComponent<ReactCalendar> {

    private final SelenideElement input = $(".react-datepicker__input-container input");
    private final SelenideElement previousMonthButton = self.$(".react-datepicker__navigation--previous");
    private final SelenideElement nextMonthButton = self.$(".react-datepicker__navigation--next");
    private final SelenideElement currentMonthCurrentYear = self.$(".react-datepicker__current-month");

    /**
     * Конструктор для создания экземпляра компонента.
     *
     * @param self Селектор для элемента компонента
     */
    public ReactCalendar(SelenideElement self) {
        super(self);
    }

    @Step("Select date in calendar: {date}")
    // по заданию метод должен принимать тип Date: selectDate(Date date),
    // но мне не зашло
    // LocalDate более современная, методы удобнее
    public ReactCalendar selectDate(LocalDate localDate) {
        openCalendar();

        String day = String.valueOf(localDate.getDayOfMonth());
        String monthName = localDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        selectTargetMonthTargetYear(localDate.getYear(), localDate.getMonthValue());
        selectDay(monthName, day);

        return this;
    }

    private void openCalendar() {
        input.click();
    }

    private void selectTargetMonthTargetYear(int targetYear, int targetMonth) {
        if (targetYear > getCurrentYear()) {
            increaseYear(targetYear);
            increaseMonth(targetMonth);
        } else {
            decreaseYear(targetYear);
            decreaseMonth(targetMonth);
        }
    }

    private void increaseYear(int targetYear) {
        while (getCurrentYear() < targetYear) {
            nextMonthButton.click();
            Selenide.sleep(200);
        }
    }

    private void decreaseYear(int targetYear) {
        while (getCurrentYear() > targetYear) {
            previousMonthButton.click();
            Selenide.sleep(200);
        }
    }

    private void increaseMonth(int targetMonthNumber) {
        while (getCurrentMonthNumber() < targetMonthNumber) {
            nextMonthButton.click();
            Selenide.sleep(200);
        }
    }

    private void decreaseMonth(int targetMonthNumber) {
        while (getCurrentMonthNumber() > targetMonthNumber) {
            previousMonthButton.click();
            Selenide.sleep(200);
        }
    }

    private void selectDay(String targetMonth, String targetDay) {
        ElementsCollection days = $$(".react-datepicker__day");
        // Фильтруем дни, которые содержат целевую строку в атрибуте aria-label
        List<SelenideElement> matchingDays =
                days.asDynamicIterable().stream()
                        .filter(day -> Objects.requireNonNull(
                                day.getAttribute("aria-label")).contains(String.format("%s %s", targetMonth, targetDay)))
                        .toList();
        // Выбираем последний день из отфильтрованной коллекции
        matchingDays.getLast().click();
    }

    public int getCurrentYear() {
        return Integer.parseInt(currentMonthCurrentYear.getText().split(" ")[1]);
    }

    private String getCurrentMonth() {
        return currentMonthCurrentYear.getText().split(" ")[0].toUpperCase();
    }

    public String getSelectedDay() {
        ElementsCollection days = $$(".react-datepicker__day");
        // Фильтруем дни, которые содержат целевую строку "true" в атрибуте "aria-selected"
        try {
            List<SelenideElement> matchingDays =
                    days.asDynamicIterable().stream()
                            .filter(day -> Objects.requireNonNull(
                                    day.getAttribute("aria-selected")).contains("true")).toList();

            return matchingDays.getLast().getText();
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Дата не выбрана");
        }
    }

    public int getCurrentMonthNumber() {
        return Arrays.stream(Month.values())
                .filter(month -> month.name().equals(getCurrentMonth()))
                .findFirst()
                .map(Month::ordinal)
                .map(i -> i + 1) // добавляем 1, чтобы получить номер месяца в формате 1-12
                .orElse(-1);
    }

    // Из ui-календаря получает выбранную дату в формате "yyyy-mm-dd"
    public String getSelectedDate() {
        String selectedDay = getSelectedDay();
        String selectedMonth = String.valueOf(getCurrentMonthNumber());
        String selectedYear = String.valueOf(getCurrentYear());
        String[] parts = String.format("%s-%s-%s", selectedYear, selectedMonth, selectedDay).split("-");
        return String.format("%04d-%02d-%02d", Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }
}