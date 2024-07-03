package guru.qa.niffler.data.repository.logging;


import io.qameta.allure.attachment.AttachmentData;

/**
 * Класс предназначен для создания вложений (attachments) в отчетах Allure, содержащих информацию о SQL-запросах,
 * выполненных в ходе тестирования.
 */
public class SqlRequestAttachment implements AttachmentData {

    private final String name;
    private final String sql;

    public SqlRequestAttachment(String name, String sql) {
        this.name = name;
        this.sql = sql;
    }

    /**
     * @return имя вложения
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return текст SQL-запроса
     */
    public String getSql() {
        return sql;
    }
}
