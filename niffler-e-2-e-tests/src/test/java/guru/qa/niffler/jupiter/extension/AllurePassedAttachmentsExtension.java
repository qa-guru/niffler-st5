package guru.qa.niffler.jupiter.extension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.qameta.allure.model.Status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static guru.qa.niffler.jupiter.extension.AllureLogsExtension.caseName;

/**
 * Добавление пустых вложений (attachments) к успешным тестовым случаям в отчетах Allure :)
 * Переписывается в result.json массив "attachments" на пустой, если статус Passed
 */
public class AllurePassedAttachmentsExtension implements SuiteExtension {

    /**
     * Экземпляр ObjectMapper из библиотеки Jackson, используемый для чтения и записи JSON-данных.
     */
    private static final ObjectMapper om = new ObjectMapper();

    /**
     * Путь к директории, содержащей результаты Allure
     */
    private final String pathToAllureResults = "./niffler-e-2-e-tests/build/allure-results";

    /**
     * Получает список всех файлов с результатами тестов в директории pathToAllureResults.
     * Фильтрует файлы, заканчивающиеся на "-result.json".
     * Для каждого файла с результатами тестов:
     * Читает JSON-данные из файла с помощью ObjectMapper.
     * Проверяет, что статус теста - PASSED и имя теста не равно caseName (определенному в AllureLogsExtension).
     * Если условие выше выполняется, добавляет пустой массив attachments в JSON-данные.
     * Записывает обновленные JSON-данные обратно в файл.
     */
    @Override
    public void afterSuite() {
        try (Stream<Path> files = Files.walk(Path.of(pathToAllureResults))) {
            List<Path> allureResults = files
                    .filter(Files::isRegularFile)
                    .filter(f -> f.getFileName().toString().endsWith("-result.json"))
                    .toList();

            for (Path allureResult : allureResults) {
                JsonNode jsonResult = om.readTree(Files.newInputStream(allureResult));
                if (jsonResult.get("status") != null
                        && jsonResult.get("status").asText().equals(Status.PASSED.value())
                        && jsonResult.get("testCaseName") != null
                        && !jsonResult.get("testCaseName").asText().equals(caseName)) {
                    ((ObjectNode) jsonResult).putArray("attachments");
                    Files.write(
                            Path.of(pathToAllureResults + "/" + jsonResult.get("uuid").asText() + "-result.json"),
                            om.writeValueAsBytes(jsonResult)
                    );
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
