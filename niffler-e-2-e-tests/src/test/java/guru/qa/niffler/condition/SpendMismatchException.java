package guru.qa.niffler.condition;

import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.impl.CollectionSource;

import javax.annotation.Nullable;

import static java.lang.System.lineSeparator;

public class SpendMismatchException extends UIAssertionError {
    public SpendMismatchException(String message, CollectionSource collection,
                                  String expectedSpend, String actualElementText,
                                  @Nullable String explanation, long timeoutMs,
                                  @Nullable Throwable cause) {
        super(
                collection.driver(),
                message +
                        lineSeparator() + "Actual: " + actualElementText +
                        lineSeparator() + "Expected: " + expectedSpend +
                        (explanation == null ? "" : lineSeparator() + "Because: " + explanation) +
                        lineSeparator() + "Collection: " + collection.description(),
                expectedSpend, actualElementText,
                cause,
                timeoutMs);
    }
}
