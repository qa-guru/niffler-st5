package guru.qa.niffler.api;

import guru.qa.niffler.api.cookie.ThreadSafeCookieStore;
import guru.qa.niffler.api.interceptor.CodeInterceptor;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.oauth.TokenJson;
import guru.qa.niffler.utils.OauthUtils;
import lombok.SneakyThrows;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class AuthApiClient extends ApiClient {

    private final AuthApi authApi;

    public AuthApiClient() {
        super(
                CFG.authUrl(),
                true,
                JacksonConverterFactory.create(),
                HttpLoggingInterceptor.Level.BODY,
                new CodeInterceptor()
        );
        this.authApi = retrofit.create(AuthApi.class);
    }

    @SneakyThrows
    public void doLogin(String username, String password) {
        final String codeVerifier = OauthUtils.generateCodeVerifier();
        final String codeChallenge = OauthUtils.generateCodeChallange(codeVerifier);

        authApi.preRequest(
                "code",
                "client",
                "openid",
                CFG.frontUrl() + "/authorized",
                codeChallenge,
                "S256"
        ).execute();

        authApi.login(
                ThreadSafeCookieStore.INSTANCE.getCookieValue("XSRF-TOKEN"), // значение куки XSRF-TOKEN
                username,
                password
        ).execute();

        TokenJson response = authApi.token(
                ApiLoginExtension.getCode(),
                CFG.frontUrl() + "/authorized", //
                codeVerifier,
                "authorization_code",
                "client"
        ).execute().body();

        ApiLoginExtension.setToken(response.idToken());
    }
}
