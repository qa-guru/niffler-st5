package guru.qa.niffler.data;

import guru.qa.niffler.config.Config;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DataBase {
    AUTH("jdbc:postgresql://%s:%d/niffler-auth"),
    CURRENCY("jdbc:postgresql://%s:%d/niffler-currency"),
    SPEND("jdbc:postgresql://%s:%d/niffler-spend"),
    USERDATA("jdbc:postgresql://%s:%d/niffler-userdata");

    private static final Config CFG = Config.getInstance();
    private final String jdbsUrl;

    public String getJdbsUrl() {
        return String.format(
                jdbsUrl,
                CFG.dbHost(),
                CFG.dbPort());
    }
}