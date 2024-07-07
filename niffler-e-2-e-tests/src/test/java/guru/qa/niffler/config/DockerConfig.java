package guru.qa.niffler.config;

public class DockerConfig implements Config {

    static final DockerConfig instance = new DockerConfig();

    @Override
    public String frontUrl() {
        return "http://front.niffler.dc/";
    }

    @Override
    public String dbHost() {
        return "niffler-all-db";
    }

    @Override
    public String spendUrl() {
        return "http://spend.niffler.dc:8093";
    }
}

