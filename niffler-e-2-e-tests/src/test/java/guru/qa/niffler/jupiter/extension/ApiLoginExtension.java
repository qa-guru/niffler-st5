package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.api.cookie.ThreadSafeCookieStore;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.niffler.jupiter.extension.ContextExtension.context;

/**
 * Расширение JUnit Jupiter для автоматической авторизации в API перед каждым тестом.
 * Использует аннотацию {@link ApiLogin} для получения учетных данных.
 */
public class ApiLoginExtension implements BeforeEachCallback, AfterEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ApiLoginExtension.class);
    private final AuthApiClient authApiClient = new AuthApiClient();

    /**
     * Выполняет вход в систему перед каждым тестовым методом, если присутствует аннотация {@link ApiLogin}.
     *
     * @param context контекст теста
     * @throws Exception если возникает ошибка при выполнении входа в систему
     */
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                ApiLogin.class
        ).ifPresent(annotation -> {
            authApiClient.doLogin(annotation.username(), annotation.password());
        });
    }

    /**
     * Очищает cookie после каждого тестового метода.
     *
     * @param context контекст теста
     * @throws Exception если возникает ошибка при очистке cookie
     */
    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        ThreadSafeCookieStore.INSTANCE.clearCookies();
    }

    /**
     * Сохраняет токен в контексте теста.
     *
     * @param token токен для сохранения
     */
    public static void setToken(String token) {
        context().getStore(NAMESPACE).put("token", token);
    }

    /**
     * Получает сохраненный токен из контекста теста.
     *
     * @return сохраненный токен
     */
    public static String getToken() {
        return context().getStore(NAMESPACE).get("token", String.class);
    }

    /**
     * Сохраняет проверочный код для PKCE в контексте теста.
     *
     * @param codeChallenge проверочный код для сохранения
     */
    public static void setCodeChallenge(String codeChallenge) {
        context().getStore(NAMESPACE).put("cc", codeChallenge);
    }

    /**
     * Получает сохраненный проверочный код для PKCE из контекста теста.
     *
     * @return сохраненный проверочный код
     */
    public static String getCodeChallenge() {
        return context().getStore(NAMESPACE).get("cc", String.class);
    }

    /**
     * Сохраняет код верификации для PKCE в контексте теста.
     *
     * @param codeVerifier код верификации для сохранения
     */
    public static void setCodeVerifier(String codeVerifier) {
        context().getStore(NAMESPACE).put("cv", codeVerifier);
    }

    /**
     * Получает сохраненный код верификации для PKCE из контекста теста.
     *
     * @return сохраненный код верификации
     */
    public static String getCodeVerifier() {
        return context().getStore(NAMESPACE).get("cv", String.class);
    }

    /**
     * Сохраняет код авторизации в контексте теста.
     *
     * @param code код авторизации для сохранения
     */
    public static void setCode(String code) {
        context().getStore(NAMESPACE).put("c", code);
    }

    /**
     * Получает сохраненный код авторизации из контекста теста.
     *
     * @return сохраненный код авторизации
     */
    public static String getCode() {
        return context().getStore(NAMESPACE).get("c", String.class);
    }
}