package guru.qa.niffler.config;

public class LocalConfig implements Config {

    static final LocalConfig instance = new LocalConfig();

    private static final String URL = "http://127.0.0.1:";

    @Override
    public String frontUrl() {return URL + "3000/";}

    @Override
    public String spendUrl() {
        return URL + "8093/";
    }

    @Override
    public String gatewayUrl() {
        return URL + "8090/";
    }

    @Override
    public String dbHost() {return "127.0.0.1";}

}