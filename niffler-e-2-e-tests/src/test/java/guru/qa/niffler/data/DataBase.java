package guru.qa.niffler.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DataBase {
    AUTH("jdbc:postgresql://localhost:5432/niffler-auth"),
    CURRENCY("jdbc:postgresql://localhost:5432/niffler-currency"),
    SPEND("jdbc:postgresql://localhost:5432/niffler-spend"),
    USERDATA("jdbc:postgresql://localhost:5432/niffler-userdata");

    private final String jdbcUrl;
}
