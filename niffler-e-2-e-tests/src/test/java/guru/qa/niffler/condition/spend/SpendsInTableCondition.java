package guru.qa.niffler.condition.spend;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.condition.MismatchException;
import guru.qa.niffler.model.SpendJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class SpendsInTableCondition extends WebElementsCondition {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yy", Locale.ENGLISH);

    private final SpendJson[] expectedSpends;

    public SpendsInTableCondition(SpendJson[] expectedSpends) {
        this.expectedSpends = expectedSpends;
    }

    public CheckResult check(Driver driver, List<WebElement> elements) {
        if (elements.size() != expectedSpends.length) {
            return CheckResult.rejected("Spending table size mismatch", elements.size());
        }

        for (int i = 0; i < elements.size(); i++) {
            WebElement row = elements.get(i);
            SpendJson expectedSpendForRow = expectedSpends[i];
            List<WebElement> td = row.findElements(By.cssSelector("td"));

            CheckResult result = validate(td.get(1).getText(), DATE_FORMAT.format(expectedSpendForRow.spendDate()), "Spending table: date mismatch");
            if (!Objects.equals(result, CheckResult.accepted())) return result;

            result = validate(td.get(2).getText(), String.valueOf(expectedSpendForRow.amount().intValue()), "Spending table: amount mismatch");
            if (!Objects.equals(result, CheckResult.accepted())) return result;

            result = validate(td.get(3).getText(), expectedSpendForRow.currency().name(), "Spending table: currency mismatch");
            if (!Objects.equals(result, CheckResult.accepted())) return result;

            result = validate(td.get(4).getText(), expectedSpendForRow.category(), "Spending table: category mismatch");
            if (!Objects.equals(result, CheckResult.accepted())) return result;

            result = validate(td.get(5).getText(), expectedSpendForRow.description(), "Spending table: description mismatch");
            if (!Objects.equals(result, CheckResult.accepted())) return result;
        }

        return CheckResult.accepted();
    }

    @Override
    public void fail(CollectionSource collection, CheckResult lastCheckResult, @Nullable Exception cause, long timeoutMs) {
        String actual = collection.getElements().stream()
                .map(element -> "\n- " + element.getText().replace("\n", " | "))
                .collect(Collectors.joining());

        String expected = Arrays.stream(expectedSpends)
                .map(spendJson -> String.format(
                        "\n- %s | %s | %s | %s | %s",
                        DATE_FORMAT.format(spendJson.spendDate()),
                        spendJson.amount().intValue(),
                        spendJson.currency().name(),
                        spendJson.category(),
                        spendJson.description()
                ))
                .collect(Collectors.joining());

        String message = lastCheckResult.getMessageOrElse(() -> "Spending mismatch");
        throw new MismatchException(message, collection, expected, actual, explanation, timeoutMs, cause);
    }

    private CheckResult validate(String actual, String expected, String errorMessage) {
        if (!actual.equals(expected)) {
            return CheckResult.rejected(errorMessage, actual);
        }

        return CheckResult.accepted();
    }

    @Override
    public String toString() {
        return "";
    }

}