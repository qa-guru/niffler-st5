package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.TestResult;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static io.qameta.allure.model.Status.PASSED;

/**
 *  Реализует интерфейс SuiteExtension.
 *  Основная функциональность этого класса - прикрепление лог-файлов к отчетам Allure после завершения выполнения всех тестов в наборе.
 *  Использование SuiteExtension позволяет выполнять эту задачу в централизованном месте, не загрязняя код тестов дополнительной логикой.
 */
public class AllureLogsExtension implements SuiteExtension {

    public static final String caseName = "logs";

    /**
     * Генерирует уникальный идентификатор caseId для тестового случая, который будет использоваться для прикрепления лог-файлов.
     * Получает экземпляр AllureLifecycle из библиотеки Allure.
     * Создает новый тестовый случай с именем caseName и статусом PASSED.
     * Начинает выполнение тестового случая.
     * Прикрепляет лог-файлы для различных компонентов приложения (auth, currency, gateway, spend, userdata) к отчету Allure.
     * Завершает выполнение тестового случая.
     *
     * @SneakyThrows используется для обработки исключений, связанных с чтением лог-файлов.
     * Это позволяет избежать необходимости обрабатывать исключения в самом методе afterSuite()
     */
    @SneakyThrows
    @Override
    public void afterSuite() {
        String caseId = UUID.randomUUID().toString();
        AllureLifecycle lifecycle = Allure.getLifecycle();
        lifecycle.scheduleTestCase(new TestResult().setUuid(caseId).setName(caseName));
        lifecycle.startTestCase(caseId);

        lifecycle.addAttachment("auth log", "text/html", ".log", Files.newInputStream(
                Path.of("./auth.log")));
        lifecycle.addAttachment("currency log", "text/html", ".log", Files.newInputStream(
                Path.of("./currency.log")));
        lifecycle.addAttachment("gateway log", "text/html", ".log", Files.newInputStream(
                Path.of("./gateway.log")));
        lifecycle.addAttachment("spend log", "text/html", ".log", Files.newInputStream(
                Path.of("./spend.log")));
        lifecycle.addAttachment("userdata log", "text/html", ".log", Files.newInputStream(
                Path.of("./userdata.log")));

        lifecycle.startTestCase(caseId);
        lifecycle.writeTestCase(caseId);
    }
}
