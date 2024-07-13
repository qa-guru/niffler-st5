package guru.qa.niffler.condition.user;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.condition.MismatchException;
import guru.qa.niffler.model.UserJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsersInTableCondition extends WebElementsCondition {

    private final UserJson[] expectedUsers;

    public UsersInTableCondition(UserJson[] expectedUsers) {
        this.expectedUsers = expectedUsers;
    }

    @Nonnull
    @Override
    public CheckResult check(Driver driver, List<WebElement> elements) {
        for (UserJson expectedUser : expectedUsers) {
            Optional<List<WebElement>> matchingElement = elements.stream()
                    .map(e -> e.findElements(By.cssSelector("td")))
                    .filter(td -> td.get(1).getText().equals(expectedUser.username()))
                    .findFirst();

            if (matchingElement.isEmpty()) {
                String actual = "\n" + elements.stream().map(WebElement::getText).collect(Collectors.joining("\n"));
                return CheckResult.rejected("User not found by username", actual);
            } else {
                String name = matchingElement.get().get(2).getText();
                if (name != null || (expectedUser.firstname() != null && expectedUser.surname() != null)) {
                    if (!name.equals(expectedUser.firstname() + " " + expectedUser.surname()))
                        return CheckResult.rejected("User not found by name", name);
                }
            }
        }

        return CheckResult.accepted();
    }

    @Override
    public void fail(CollectionSource collection, CheckResult lastCheckResult, @Nullable Exception cause, long timeoutMs) {
        String actual = lastCheckResult.getActualValueOrElse("Actual value is null");

        String expected = Arrays.stream(expectedUsers)
                .map(userJson -> String.format(
                        "\n username: %s, \n name: %s",
                        userJson.username(), userJson.firstname() + " " + userJson.surname()
                ))
                .collect(Collectors.joining());

        String message = lastCheckResult.getMessageOrElse(() -> "Users mismatch");
        throw new MismatchException(message, collection, expected, actual, explanation, timeoutMs, cause);
    }

    @Override
    public String toString() {
        return "";
    }

}