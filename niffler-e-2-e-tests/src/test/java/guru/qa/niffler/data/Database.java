package guru.qa.niffler.data;

import guru.qa.niffler.config.Config;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;


@RequiredArgsConstructor
public enum Database {

    AUTH("jdbc:postgresql://%s:%d/niffler-auth"),
    CURRENCY("jdbc:postgresql://%s:%d/niffler-currency"),
    SPEND("jdbc:postgresql://%s:%d/niffler-spend"),
    USERDATA("jdbc:postgresql://%s:%d/niffler-userdata");

    private static final Config CONFIG = Config.getInstance();
    private final String jdbcUrl;

    public String getJdbcUrl() {
        return String.format(
                jdbcUrl,
                CONFIG.dbHost(),
                CONFIG.dbPort()
        );
    }

    public String getP6SpyUrl(){
        return "jdbc:p6spy:" + StringUtils.substringAfter(getJdbcUrl(), ":");
    }

}