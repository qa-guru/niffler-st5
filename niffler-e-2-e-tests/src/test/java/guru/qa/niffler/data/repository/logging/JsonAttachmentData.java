package guru.qa.niffler.data.repository.logging;

import io.qameta.allure.attachment.AttachmentData;

public class JsonAttachmentData implements AttachmentData {
    private final String name;
    private final String data;

    public JsonAttachmentData(String name, String data) {
        this.name = name;
        this.data = data;
    }

    /**
     * @return имя вложения
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return Json-запроса
     */
    public String getData() {
        return data;
    }
}
