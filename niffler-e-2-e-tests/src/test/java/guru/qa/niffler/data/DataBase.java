package guru.qa.niffler.data;

import guru.qa.niffler.config.Config;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor //объявляем конструктор для работы полей
public enum DataBase {
    AUTH("jdbc:postgresql://%s:%d/niffler-auth"),
    CURRENCY("jdbc:postgresql://%s:%d/niffler-currency"),
    SPEND("jdbc:postgresql://%s:%d/niffler-spend"),
    USERDATA("jdbc:postgresql://%s:%d/niffler-userdata");

    private static final Config CFG = Config.getInstance();
    private final String jdbcUrl;

    public String getJdbcUrl() {
        return String.format(
                jdbcUrl,
                CFG.dbHost(),
                CFG.dbPort()
        );
    }


    /**
     * Модифицирует JDBC URL, полученный из getJdbcUrl(), добавляя префикс "jdbc:p6spy:",
     * чтобы P6Spy могла перехватывать и логировать все SQL-запросы
     * @return JDBC URL, который может быть использован с библиотекой P6Spy
     */
    public String getP6spyUrl() {
        return "jdbc:p6spy:" + StringUtils.substringAfter(getJdbcUrl(), ":");
        // P6Spy - инструмент для мониторинга и логирования SQL-запросов, выполняемых приложением.
    }
}
