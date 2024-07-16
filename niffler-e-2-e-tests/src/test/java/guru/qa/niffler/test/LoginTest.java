package guru.qa.niffler.test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class LoginTest {

    private static final WireMockServer wiremockServer = new WireMockServer(
            wireMockConfig()
                    .port(8077)
                    .globalTemplating(true)
                    .stubCorsEnabled(true)
                    .extensions(new FooTransformer())
    );

    @BeforeAll
    static void start() {
        wiremockServer.start();
    }

    @AfterAll
    static void stop() {
        wiremockServer.stop();
    }

    @BeforeEach
    void configure() {
        new WireMock(8077).register(get(urlPathEqualTo("/internal/orders"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("foo.json")
                        .withTransformers("foo-transformer"))
        );
    }

    @Test
    void loginTest() {
        while (true) {

        }
    }
}
