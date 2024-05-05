package guru.qa.niffler.data;

public enum DataBase {
    AUTH("jdbc:postgresql://localhost:5432/niffler-auth"),
    CURRENCY("jdbc:postgresql://localhost:5432/niffler-currency"),
    SPEND("jdbc:postgresql://localhost:5432/niffler-spend"),
    USERDATA("jdbc:postgresql://localhost:5432/niffler-userdata");

    private final String jdbcUrl;

    DataBase(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }
}