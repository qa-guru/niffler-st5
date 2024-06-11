package guru.qa.niffler.data;

import guru.qa.niffler.config.Config;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
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

    public String getP6spyUrl() {
        return "jdbc:p6spy:" + StringUtils.substringAfter(getJdbcUrl(), ":");
    }
}
