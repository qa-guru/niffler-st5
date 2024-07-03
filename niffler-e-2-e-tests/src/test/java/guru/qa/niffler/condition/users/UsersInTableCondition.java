package guru.qa.niffler.condition.users;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.FriendState;
import guru.qa.niffler.model.UserJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class UsersInTableCondition extends WebElementsCondition {

    private final UserJson[] expectedUsers;

    public UsersInTableCondition(UserJson[] expectedUsers) {
        this.expectedUsers = expectedUsers;
    }

    @Nonnull
    @Override
    public CheckResult check(Driver driver, List<WebElement> elements) {

        //Создается StringBuilder для построения строки с фактическими расходами
        StringBuilder actualUsersBuilder = new StringBuilder("Actual users:\n");
        boolean allMatch = true;

        // минусую 'шапку таблицы' (чтобы свалить тест и посмотреть отчёт, можно как раз тут поменять)
        if (elements.size() - 1 != expectedUsers.length) {
            allMatch = false;
        }

        for (int i = 1; i < elements.size(); i++) {
            WebElement row = elements.get(i);

            // порядок в массивах может отличаться, так что забираю индекс объекта по имени, вместо сортировки
            int j = findIndexOfExpectedUser(row, expectedUsers);
            UserJson expectedUserForRow = expectedUsers[j];

            // Для каждой строки таблицы вызывается метод compareUser(), чтобы проверить, что она соответствует ожидаемому другу.
            // Если хотя бы одна строка не соответствует, allMatch устанавливается в false.
            if (!compareUser(expectedUserForRow, row)) {
                allMatch = false;
            }

            String webAction = "";
            if (row.findElement(By.cssSelector("td:nth-child(4)")).findElements(By.tagName("div")).get(1).getAttribute("data-tooltip-id") != null) {
                webAction = row.findElement(By.cssSelector("td:nth-child(4)")).findElements(By.tagName("div")).get(1).getAttribute("data-tooltip-id");
            } else if (!row.findElement(By.cssSelector("td:nth-child(4)")).findElements(By.tagName("div")).get(1).getText().isEmpty()) {
                webAction = row.findElement(By.cssSelector("td:nth-child(4)")).findElements(By.tagName("div")).get(1).getText();
            }

            String action = "";
            action = switch (webAction) {
                case "You are friends" -> String.valueOf(FriendState.FRIEND);
                case "add-friend" -> null;
                case "Pending invitation" -> String.valueOf(FriendState.INVITE_SENT);
                case "submit-invitation" -> String.valueOf(FriendState.INVITE_RECEIVED);
                default -> throw new IllegalStateException("Unexpected value: " + webAction);
            };

            actualUsersBuilder.append("— ")
                    .append(row.findElement(By.cssSelector("td:nth-child(1)")).findElement(By.tagName("img"))
                            .getAttribute("src").replaceAll("^.*?/([^/]+)$", "images/$1"))
                    .append(" | ")
                    .append(row.findElement(By.cssSelector("td:nth-child(2)")).getText())
                    .append(" | ")
                    .append(row.findElement(By.cssSelector("td:nth-child(3)")).getText())
                    .append(" | ")
                    .append(action)
                    .append("\n");
        }

        // Построенная строка с фактическими расходами сохраняется в переменную
        String actualUsersText = actualUsersBuilder.toString();

        if (!allMatch) {
            // Если allMatch равен false, то возвращается CheckResult.rejected() с сообщением об ошибке и фактическими расходами.
            return CheckResult.rejected("Users mismatch:\n", actualUsersText);
        }

        return CheckResult.accepted();
    }

    /**
     * Сравнивает ожидаемого пользователя с данными в строке таблицы.
     *
     * @param expectedUser
     * @param row
     * @return
     */
    private boolean compareUser(UserJson expectedUser, WebElement row) {
        List<WebElement> td = row.findElements(By.cssSelector("td"));

        boolean photoSmallResult;
        String photo = td.get(0).findElement(By.cssSelector("img")).getAttribute("src");

        if (photo.contains("images/niffler_avatar.jpeg"))
            photoSmallResult = expectedUser.photoSmall() == null;
        else
            photoSmallResult = expectedUser.photoSmall().equals(photo);

        boolean usernameResult = td.get(1).getText().equals(
                expectedUser.username());

        boolean firstnameResult;
        if (expectedUser.surname() != null) {
            firstnameResult = td.get(2).getText().equals(
                    formatNameSurname(String.format("%s %S", expectedUser.firstname(), expectedUser.surname())));
        } else {
            firstnameResult = td.get(2).getText().equals((expectedUser.firstname()));
        }

        boolean friendState;
        String webAction = "";

        if (td.get(3).findElements(By.tagName("div")).get(1).getAttribute("data-tooltip-id") != null) {
            webAction = td.get(3).findElements(By.tagName("div")).get(1).getAttribute("data-tooltip-id");
        } else if (!td.get(3).findElements(By.tagName("div")).get(1).getText().isEmpty()) {
            webAction = td.get(3).findElements(By.tagName("div")).get(1).getText();
        }

        friendState = switch (webAction) {
            case "You are friends" -> expectedUser.friendState().equals(FriendState.FRIEND);
            case "add-friend" -> expectedUser.friendState() == null;
            case "Pending invitation" -> expectedUser.friendState().equals(FriendState.INVITE_SENT);
            case "submit-invitation" -> expectedUser.friendState().equals(FriendState.INVITE_RECEIVED);
            default -> false;
        };

        return photoSmallResult && usernameResult && firstnameResult && friendState;
    }

    /**
     * Вызывается, когда проверка не проходит.
     * Создает исключение UserMismatchException с подробной информацией об ошибке.
     *
     * @param collection
     * @param lastCheckResult
     * @param cause
     * @param timeoutMs
     */
    @Override
    public void fail(CollectionSource collection, CheckResult lastCheckResult, @Nullable Exception cause, long timeoutMs) {
        String actualElementText = lastCheckResult.getActualValue();
        String expectedUserText = formatExpectedUsers(expectedUsers);
        String message = lastCheckResult.getMessageOrElse(() -> "Users mismatch:");

        UserMismatchException exception = new UserMismatchException(
                message,
                collection,
                expectedUserText,
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
     * @param expectedUsers
     * @return
     */
    private String formatExpectedUsers(UserJson[] expectedUsers) {


        StringBuilder sb = new StringBuilder("Expected Users:\n");
        for (UserJson user : expectedUsers) {

            String uiUserName;
            if (user.surname() != null) {
                uiUserName = String.format("%s %S", user.firstname(), user.surname());
            } else {
                uiUserName = user.firstname();
            }

            sb.append("— ")
                    .append(user.photoSmall() == null ? "images/niffler_avatar.jpeg" : user.photoSmall())
                    .append(" | ")
                    .append(user.username())
                    .append(" | ")
                    .append(formatNameSurname(uiUserName))
                    .append(" | ")
                    .append(user.friendState())
                    .append("\n");
        }
        return sb.toString();
    }

    private int findIndexOfExpectedUser(WebElement row, UserJson[] expectedUsers) {
        String username = row.findElements(By.cssSelector("td")).get(1).getText();
        for (int i = 0; i < expectedUsers.length; i++) {
            if (expectedUsers[i].username().equals(username)) {
                return i;
            }
        }
        return -1; // Не найден соответствующий пользователь
    }

    // форматирует строку "Имя ФАМИЛИЯ" в "Имя Фамилия"
    public static String formatNameSurname(String input) {
        if (input == null) return "";

        String[] parts = input.split(" ");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (!result.isEmpty()) {
                result.append(" ");
            }

            if (part.contains("'")) {
                // Имя с апострофом
                String[] nameParts = part.split("'");
                result.append(Character.toUpperCase(nameParts[0].charAt(0)));
                result.append(nameParts[0].substring(1).toLowerCase());
                result.append("'");
                result.append(Character.toUpperCase(nameParts[1].charAt(0)));
                if (nameParts[1].length() > 1) {
                    result.append(nameParts[1].substring(1).toLowerCase());
                }
            } else {
                // Обычное имя
                result.append(Character.toUpperCase(part.charAt(0)));
                if (part.length() > 1) {
                    result.append(part.substring(1).toLowerCase());
                }
            }
        }

        return result.toString();
    }

    @Override
    public String toString() {
        return "";
    }
}
