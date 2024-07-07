package guru.qa.niffler.config;

public interface Config {

    static Config getInstance(){
        if("local".equals(System.getProperty("test.env", "local"))) {
            return LocalConfig.instance;
        }
        if("docker".equals(System.getProperty("test.env"))) {
            return DockerConfig.instance;
        }
        throw new RuntimeException("Unknown config");
    }
    String frontUrl();

    String dbHost();

    String spendUrl();

    default int dbPort() {
        return 5432;
    }
}
