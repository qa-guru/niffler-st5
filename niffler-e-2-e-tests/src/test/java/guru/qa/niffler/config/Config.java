package guru.qa.niffler.config;

public interface Config {

    static Config getInstance() {
        String environment = System.getProperty("test.env", "local");
        if ("local".equals(environment)) {
            return LocalConfig.instance;
        } else if ("docker".equals(System.getProperty("test.env"))) {
            return DockerConfig.instance;
        } else {
            throw new IllegalArgumentException("Can't find config with given env");
        }
    }

    String frontUrl();

    String spendUrl();

    String gatewayUrl();

    String dbHost();

    default int dbPort() {
        return 5432;
    }

}