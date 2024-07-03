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

/**
 * Перехватывает и форматирует SQL-запросы, выполняемые приложением, и прикрепляет их к отчетам Allure.
 */
public class AllureSqlAppender extends StdoutLogger {

    /**
     * В контексте библиотеки Allure, шаблоны используются для определения формата и внешнего вида вложений (attachments) в отчетах.
     * Allure поддерживает шаблоны, написанные на языке FreeMarker.
     * Значение "sql-query.ftl" указывает, что для отображения информации о SQL-запросах в отчетах Allure будет использоваться шаблон с именем "sql-query.ftl".
     * Этот шаблон должен находиться в ресурсах приложения (обычно в папке src/main/resources).
     * Шаблон "sql-query.ftl" определяет, как будет выглядеть вложение с информацией о SQL-запросе в отчете Allure.
     * Он может включать HTML-разметку, CSS-стили и специальные переменные FreeMarker для вставки данных из объекта SqlRequestAttachment.
     */
    private final String templateName = "sql-query.ftl";

    /**
     * Интерфейс из библиотеки Allure, который отвечает за обработку вложений (attachments) в отчетах Allure
     */
    private final AttachmentProcessor<AttachmentData> processor = new DefaultAttachmentProcessor();

    /**
     * Метод переопределяет logSQL() из родительского класса StdoutLogger.
     * Вызывается каждый раз, когда P6Spy перехватывает SQL-запрос, выполняемый приложением
     * @param connectionId connection identifier.
     * @param now          current time.
     * @param elapsed      время (в миллисекундах), затраченное на выполнение SQL-запроса для анализа производительности и оптимизации запросов
     * @param category     the category to be used for logging.
     * @param prepared     the prepared statement to be logged.
     * @param sql          the {@code SQL} to be logged.
     * @param url          the database url where the sql statement executed
     */
    @Override
    public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared, String sql, String url) {
        if (StringUtils.isNotBlank(sql)) {
            SqlRequestAttachment sqlRequestAttachment = new SqlRequestAttachment(
                    sql.split("\\s+")[0] + StringUtils.substringBefore(url, "?"), // Имя вложения состоит из первого слова SQL-запроса и части URL-адреса до первого вопросительного знака.
                    SqlFormatter.of(Dialect.PlSql).format(sql)
            );
            // Объект SqlRequestAttachment прикрепляется к отчету Allure с помощью processor.addAttachment(), используя шаблон, определенный в templateName.
            processor.addAttachment(sqlRequestAttachment, new FreemarkerAttachmentRenderer(templateName));
        }
    }
}
