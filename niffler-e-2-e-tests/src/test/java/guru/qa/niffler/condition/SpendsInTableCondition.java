package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.SpendJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;

import static guru.qa.niffler.utils.DateHelper.formatDate;

/**
 * Класс расширяет WebElementsCondition из библиотеки Selenide.
 * Используется для проверки, что элементы таблицы (расходы) соответствуют ожидаемым значениям, представленным в виде массива SpendJson.
 * Обеспечивает подробное сообщение об ошибке, если проверка не проходит.
 */
public class SpendsInTableCondition extends WebElementsCondition {

    private final SpendJson[] expectedSpends;

    public SpendsInTableCondition(SpendJson[] expectedSpends) {
        this.expectedSpends = expectedSpends;
    }

    @Nonnull
    @Override
    public CheckResult check(Driver driver, List<WebElement> elements) {
        //Создается StringBuilder для построения строки с фактическими расходами
        StringBuilder actualSpendingsBuilder = new StringBuilder("Actual spendings:\n");
        boolean allMatch = true;

        if (elements.size() != expectedSpends.length) {
            allMatch = false;
        }

        for (int i = 0; i < elements.size(); i++) {
            WebElement row = elements.get(i);
            SpendJson expectedSpendForRow = expectedSpends[i];

            // Для каждой строки таблицы вызывается метод compareSpend(), чтобы проверить, что она соответствует ожидаемому расходу.
            // Если хотя бы одна строка не соответствует, allMatch устанавливается в false.
            if (!compareSpend(expectedSpendForRow, row)) {
                allMatch = false;
            }

            actualSpendingsBuilder.append("— ")
                    .append(row.findElement(By.cssSelector("td:nth-child(2)")).getText())
                    .append(" | ")
                    .append(row.findElement(By.cssSelector("td:nth-child(3)")).getText())
                    .append(" | ")
                    .append(row.findElement(By.cssSelector("td:nth-child(4)")).getText())
                    .append(" | ")
                    .append(row.findElement(By.cssSelector("td:nth-child(5)")).getText())
                    .append(" | ")
                    .append(row.findElement(By.cssSelector("td:nth-child(6)")).getText())
                    .append("\n");
        }

        // Построенная строка с фактическими расходами сохраняется в переменную
        String actualSpendingsText = actualSpendingsBuilder.toString();

        if (!allMatch) {
            // Если allMatch равен false, то возвращается CheckResult.rejected() с сообщением об ошибке и фактическими расходами.
            return CheckResult.rejected("Spendings mismatch:\n", actualSpendingsText);
        }

        return CheckResult.accepted();
    }

    /**
     * Сравнивает ожидаемый расход с данными в строке таблицы.
     * Проверяет, что дата, сумма, валюта, категория и описание совпадают.
     *
     * @param expectedSpend
     * @param row
     * @return
     */
    private boolean compareSpend(SpendJson expectedSpend, WebElement row) {
        List<WebElement> td = row.findElements(By.cssSelector("td"));

        boolean dateResult = td.get(1).getText().equals(
                formatDate(expectedSpend.spendDate(), "dd MMM yy")
        );

        // Метод compareTo() класса BigDecimal позволяет точно сравнивать два числа, учитывая значение после запятой.
        // Это решает проблему, когда сравнение 65000 и 65000.0 возвращает false, потому что они представляют разные значения.
        // Два объекта BigDecimal, имеющие одинаковое значение, но имеющие разный масштаб (например, 2,0 и 2,00), считаются равными в этом методе.
        // Метод возвращает 0, если объекты равны.
        boolean amountResult = new BigDecimal(td.get(2).getText()).compareTo(BigDecimal.valueOf(expectedSpend.amount())) == 0;

        boolean currencyResult = td.get(3).getText().equals(
                expectedSpend.currency().name()
        );
        boolean categoryResult = td.get(4).getText().equals(
                expectedSpend.category()
        );
        boolean descriptionResult = td.get(5).getText().equals(
                expectedSpend.description()
        );

        return dateResult && amountResult && currencyResult && categoryResult && descriptionResult;
    }

    /**
     * Вызывается, когда проверка не проходит.
     * Создает исключение SpendMismatchException с подробной информацией об ошибке.
     *
     * @param collection
     * @param lastCheckResult
     * @param cause
     * @param timeoutMs
     */
    @Override
    public void fail(CollectionSource collection, CheckResult lastCheckResult, @Nullable Exception cause, long timeoutMs) {
        String actualElementText = lastCheckResult.getActualValue();
        String expectedSpendText = formatExpectedSpends(expectedSpends);
        String message = lastCheckResult.getMessageOrElse(() -> "Spendings mismatch:");

        SpendMismatchException exception = new SpendMismatchException(
                message,
                collection,
                expectedSpendText,
                actualElementText,
                explanation,
                timeoutMs,
                cause
        );

        throw exception;
    }

    /**
     * Вспомогательный метод.
     * Форматирует ожидаемые расходы в строку для включения в сообщение об ошибке.
     *
     * @param expectedSpends
     * @return
     */
    private String formatExpectedSpends(SpendJson[] expectedSpends) {
        StringBuilder sb = new StringBuilder("Expected Spendings:\n");
        for (SpendJson spend : expectedSpends) {
            sb.append("— ")
                    .append(formatDate(spend.spendDate(), "dd MMM yy"))
                    .append(" | ")
                    .append(spend.amount())
                    .append(" | ")
                    .append(spend.currency())
                    .append(" | ")
                    .append(spend.category())
                    .append(" | ")
                    .append(spend.description())
                    .append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "";
    }
}