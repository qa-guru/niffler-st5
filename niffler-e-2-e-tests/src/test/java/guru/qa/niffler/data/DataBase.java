package guru.qa.niffler.data;

import guru.qa.niffler.data.config.Config;
import lombok.RequiredArgsConstructor;

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
}
