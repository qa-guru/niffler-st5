package guru.qa.niffler.data.repository.logging;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.github.vertical_blank.sqlformatter.languages.Dialect;
import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.StdoutLogger;
import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentProcessor;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;
import org.apache.commons.lang3.StringUtils;

public class AllureAppender extends StdoutLogger {

    private final String templateName = "sql-query.ftl";
    private final AttachmentProcessor<AttachmentData> processor = new DefaultAttachmentProcessor();

    @Override
    public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared, String sql, String url) {
        if (StringUtils.isNotBlank(sql)) {
            SqlRequestAttachment sqlRequestAttachment = new SqlRequestAttachment(
                    sql.split("\\s+")[0] + StringUtils.substringBefore(url, "?"),
                    SqlFormatter.of(Dialect.PlSql).format(sql)
            );
            processor.addAttachment(sqlRequestAttachment, new FreemarkerAttachmentRenderer(templateName));
        }
    }
}
