package guru.qa.niffler.jupiter.extension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class AllurePassedAttachExtension implements SuiteExtension {

    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public void afterSuite() {
        String pathToAllureResults = "./niffler-e-2-r-tests/build/allure-results";
        try (Stream<Path> files = Files.walk(Path.of(pathToAllureResults))) {
            List<Path> allureResults = files
                    .filter(Files::isRegularFile)
                    .filter(file -> file.getFileName().toString().endsWith("-result.json"))
                    .toList();

            for (Path allureResult : allureResults) {
                JsonNode jsonResult = mapper.readTree(Files.newInputStream(allureResult));
                if (jsonResult.get("status").asText().equals("passed")) {
                    ((ObjectNode) jsonResult).putArray("attachments");
                }
                Files.write(
                        Path.of(pathToAllureResults + "/" + jsonResult.get("uuid").asText() + "-result.json"),
                        mapper.writeValueAsBytes(jsonResult)
                );
            }
        }
    }

}
