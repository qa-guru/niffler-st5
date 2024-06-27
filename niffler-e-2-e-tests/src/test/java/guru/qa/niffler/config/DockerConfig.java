package guru.qa.niffler.config;

public class DockerConfig implements Config {

    static final DockerConfig instance = new DockerConfig();

    @Override
    public String frontUrl() {
        return "http://frontend.niffler.dc/";
    }

    @Override
    public String spendUrl() {
        return "http://spend.niffler.dc:8093/";
    }

    @Override
    public String gatewayUrl() {
        return  "http:/gateway.niffler.dc:8090/";
    }

    @Override
    public String dbHost() {
        return "niffler-all-db";
    }

}