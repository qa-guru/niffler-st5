package guru.qa.niffler.config;

public interface Config {

    static Config getInstance() {
        if ("local".equals(System.getProperty("test.env", "local"))) {
            return LocalConfig.instance;
        } else if ("docker".equals(System.getProperty("test.env"))) {
            return DockerConfig.instance;
        } else {
            throw new IllegalStateException("Can not find Config for given env");
        }
    }

    String frontUrl();

    String gatewayUrl();

    String authUrl();

    String spendUrl();

    String userdataUrl();

    String dbHost();

    default int dbPort() {
        return 5432;
    }
}
