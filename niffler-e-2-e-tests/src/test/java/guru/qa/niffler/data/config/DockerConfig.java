package guru.qa.niffler.data.config;

public class DockerConfig implements Config {

    static final DockerConfig instance = new DockerConfig();

    private DockerConfig() {
    }

    //данные берём из docker-compose.test.yml

    @Override
    public String frontUrl() {
        return "http://frontend.niffler.dc/"; // дефолтный порт 80 не указываем
    }

    @Override
    public String spendUrl() {
        return "http://spend.niffler.dc:8093/";
    }

    @Override
    public String dbHost() {
        return "niffler-all-db";
    }
}
