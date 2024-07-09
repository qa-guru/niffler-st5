package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SessionStorage;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.api.cookie.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.utils.OauthUtils;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.openqa.selenium.Cookie;

import java.util.List;
import java.util.Map;

import static guru.qa.niffler.jupiter.extension.ContextExtension.context;

public class ApiLoginExtension implements BeforeEachCallback, AfterEachCallback {

    private static final Config CFG = Config.getInstance();

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ApiLoginExtension.class);
    private final AuthApiClient authApiClient = new AuthApiClient();
    private final boolean initBrowser;

    public ApiLoginExtension() {
        this(true);
    }

    public ApiLoginExtension(boolean initBrowser) {
        this.initBrowser = initBrowser;
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ApiLogin apiLogin = AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                ApiLogin.class
        ).orElse(null);

        final String login;
        final String password;

        if (apiLogin != null) {
            TestUser testUser = apiLogin.user();
            if (!testUser.fake()) {
                UserJson createdUserForApiLogin = getCreatedUserForApiLogin();
                login = createdUserForApiLogin.username();
                password = createdUserForApiLogin.testData().password();
            } else {
                login = apiLogin.username();
                password = apiLogin.password();
            }

            final String codeVerifier = OauthUtils.generateCodeVerifier();
            final String codeChallenge = OauthUtils.generateCodeChallange(codeVerifier);
            setCodeVerifier(codeVerifier);
            setCodeChallenge(codeChallenge);
            authApiClient.doLogin(login, password);

            if (initBrowser) {
                Selenide.open(CFG.frontUrl());
                SessionStorage sessionStorage = Selenide.sessionStorage();
                sessionStorage.setItem(
                        "codeChallenge", getCodeChallenge()
                );
                sessionStorage.setItem(
                        "id_token", getToken()
                );
                sessionStorage.setItem(
                        "codeVerifier", getCodeVerifier()
                );

                WebDriverRunner.getWebDriver().manage().addCookie(
                        jsessionCookie()
                );
                Selenide.refresh();
            }
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        ThreadSafeCookieStore.INSTANCE.clearCookies();
    }

    public static void setToken(String token) {
        context().getStore(NAMESPACE).put("token", token);
    }

    public static String getToken() {
        return context().getStore(NAMESPACE).get("token", String.class);
    }

    public static void setCodeChallenge(String codeChallenge) {
        context().getStore(NAMESPACE).put("cc", codeChallenge);
    }

    public static String getCodeChallenge() {
        return context().getStore(NAMESPACE).get("cc", String.class);
    }

    public static void setCodeVerifier(String codeVerifier) {
        context().getStore(NAMESPACE).put("cv", codeVerifier);
    }

    public static String getCodeVerifier() {
        return context().getStore(NAMESPACE).get("cv", String.class);
    }

    public static void setCode(String code) {
        context().getStore(NAMESPACE).put("c", code);
    }

    public static String getCode() {
        return context().getStore(NAMESPACE).get("c", String.class);
    }

    public static String getCsrfToken() {
        return ThreadSafeCookieStore.INSTANCE.getCookieValue("XSRF-TOKEN");
    }

    public Cookie jsessionCookie() {
        return new Cookie(
                "JSESSIONID",
                ThreadSafeCookieStore.INSTANCE.getCookieValue("JSESSIONID")
        );
    }

    @SuppressWarnings("unchecked")
    private static UserJson getCreatedUserForApiLogin() {
        return ((List<UserJson>) context().getStore(CreateUserExtension.CREATE_USER_NAMESPACE).get(context().getUniqueId(), Map.class)
                .get(User.Point.INNER))
                .getFirst();
    }

}
