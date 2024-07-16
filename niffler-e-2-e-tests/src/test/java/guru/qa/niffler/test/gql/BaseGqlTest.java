package guru.qa.niffler.test.gql;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.api.GatewayGqlApiClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.GqlTest;

@GqlTest
public abstract class BaseGqlTest {

    protected static final Config CFG = Config.getInstance();
    protected static final ObjectMapper OM = new ObjectMapper();
    protected final GatewayGqlApiClient gatewayGqlApiClient = new GatewayGqlApiClient();
}
