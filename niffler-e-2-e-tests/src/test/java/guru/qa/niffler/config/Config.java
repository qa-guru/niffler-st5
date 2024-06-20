package guru.qa.niffler.config;

public interface Config {

    /**
     * Возвращает экземпляр конфигурации в зависимости от значения переменной окружения "test.env".
     * Если "test.env" равен "local", возвращает экземпляр LocalConfig.
     * Если "test.env" равен "docker", возвращает экземпляр DockerConfig.
     * В противном случае, выбрасывает исключение IllegalStateException с сообщением "No configs found".
     */
    static Config getInstance() {
        return switch (System.getProperty("test.env", "local")) {
            case "local" -> LocalConfig.instance;
            case "docker" -> DockerConfig.instance;
            default -> throw new IllegalStateException("No configs found");
        };
    }

    /**
     * Возвращает URL фронта.
     */
    String frontUrl();

    /**
     * Возвращает gateway-URL.
     */
    String gatewayUrl();

    /**
     * Возвращает URL расходов.
     */
    String spendUrl();

    /**
     * Возвращает хост базы данных.
     */
    String dbHost();

    /**
     * Возвращает auth-URL
     */
    String authUrl();

    /**
     * Возвращает порт базы данных по умолчанию (5432).
     */
    default int dbPort() {
        return 5432;
    }
}