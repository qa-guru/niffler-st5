package guru.qa.niffler.api;

import guru.qa.niffler.api.cookie.ThreadSafeCookieStore;
import guru.qa.niffler.api.interceptor.CodeInterceptor;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.oauth.TokenJson;
import guru.qa.niffler.utils.OauthUtils;
import lombok.SneakyThrows;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Клиент API для аутентификации с использованием OAuth 2.0.
 */
public class AuthApiClient extends ApiClient {

    private final AuthApi authApi;

    /**
     * Создает экземпляр клиента API для аутентификации.
     * Настраивает OkHttpClient и Retrofit с необходимыми параметрами.
     */
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

    /**
     * Выполняет вход в систему с использованием указанных имени пользователя и пароля.
     * Использует метод PKCE для безопасной аутентификации.
     *
     * @param username имя пользователя
     * @param password пароль
     * @throws Exception если возникает ошибка при выполнении входа в систему
     */
    @SneakyThrows
    public void doLogin(String username, String password) {
        final String codeVerifier = OauthUtils.generateCodeVerifier();
        final String codeChallenge = OauthUtils.generateCodeChallenge(codeVerifier);

        // Выполняем предварительный запрос на авторизацию
        authApi.preRequest(
                "code",
                "client",
                "openid",
                CFG.frontUrl() + "/authorized",
                codeChallenge,
                "S256"
        ).execute();

        // Выполняем запрос на вход в систему
        authApi.login(
                ThreadSafeCookieStore.INSTANCE.getCookieValue("XSRF-TOKEN"),
                username,
                password
        ).execute();

        // Получаем токены доступа и обновления
        TokenJson response = authApi.token(
                "Basic " + Base64.getEncoder().encodeToString("client:secret".getBytes(StandardCharsets.UTF_8)),
                ApiLoginExtension.getCode(),
                CFG.frontUrl() + "/authorized",
                codeVerifier,
                "authorization_code",
                "client"
        ).execute().body();

        // Сохраняем идентификационный токен в контексте теста
        ApiLoginExtension.setToken(response.idToken());
    }
}