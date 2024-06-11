package guru.qa.niffler.data.repository.logging;

import io.qameta.allure.attachment.AttachmentData;

public class SqlRequestAttachment implements AttachmentData {

    private final String name;
    private final String sql;

    public SqlRequestAttachment(String name, String sql) {
        this.name = name;
        this.sql = sql;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getSql() {
        return sql;
    }
}
