package guru.qa.niffler.condition.users;

import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.impl.CollectionSource;

import javax.annotation.Nullable;

import static java.lang.System.lineSeparator;

public class UserMismatchException extends UIAssertionError {
    public UserMismatchException(String message, CollectionSource collection,
                                 String expectedUserText, String actualElementText,
                                 @Nullable String explanation, long timeoutMs, @Nullable Exception cause) {
        super(
                collection.driver(),
                message +
                        lineSeparator() + "Actual: " + actualElementText +
                        lineSeparator() + "Expected: " + expectedUserText +
                        (explanation == null ? "" : lineSeparator() + "Because: " + explanation) +
                        lineSeparator() + "Collection: " + collection.description(),
                expectedUserText, actualElementText,
                cause,
                timeoutMs);
    }
}
