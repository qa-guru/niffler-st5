package guru.qa.niffler.data.repository.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;

import java.io.IOException;

/**
 * Перехватывает Json и прикрепляет к отчетам Allure.
 */
public class AllureJsonAppender {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String JSON_ATTACHMENT_TEMPLATE = "json-attachment.ftl";

    public static void attachJsonData(String name, Object data) {
        try {
            String jsonData = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
            AttachmentData attachment = new JsonAttachmentData(name, jsonData);
            new DefaultAttachmentProcessor().addAttachment(attachment, new FreemarkerAttachmentRenderer(JSON_ATTACHMENT_TEMPLATE));
        } catch (IOException e) {
            throw new RuntimeException("Failed to attach JSON data to Allure report", e);
        }
    }
}