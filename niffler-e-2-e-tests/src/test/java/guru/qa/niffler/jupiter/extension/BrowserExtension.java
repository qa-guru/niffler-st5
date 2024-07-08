package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.api.cookie.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Objects;

/**
 * Расширение JUnit Jupiter для управления браузером и сессией в тестах.
 * Настраивает Selenide, делает скриншоты ошибок, очищает сессию после каждого теста.
 */
public class BrowserExtension implements
        TestExecutionExceptionHandler,
        BeforeTestExecutionCallback,
        BeforeAllCallback,
        AfterEachCallback,
        LifecycleMethodExecutionExceptionHandler {

    /**
     * Открывает браузер и устанавливает идентификационный токен и cookie сессии перед выполнением теста.
     *
     * @param context контекст теста
     * @throws Exception если возникает ошибка при установке токена и cookie
     */
    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        if (ApiLoginExtension.getToken() != null) {
            Selenide.open(Config.getInstance().frontUrl());
            Selenide.sessionStorage().setItem("id_token", ApiLoginExtension.getToken());
            WebDriverRunner.getWebDriver().manage()
                    .addCookie(
                            new Cookie(
                                    "JSESSIONID",
                                    ThreadSafeCookieStore.INSTANCE.getCookieValue("JSESSIONID")
                            )
                    );
        }
    }

    /**
     * Обрабатывает исключения, возникающие при выполнении теста, делая скриншот перед повторным выбросом исключения.
     *
     * @param context   контекст теста
     * @param throwable возникшее исключение
     * @throws Throwable исключение, которое нужно повторно выбросить
     */
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        doScreenShot();
        throw throwable;
    }

    /**
     * Обрабатывает исключения, возникающие перед выполнением каждого метода теста, делая скриншот перед повторным выбросом исключения.
     *
     * @param context   контекст теста
     * @param throwable возникшее исключение
     * @throws Throwable исключение, которое нужно повторно выбросить
     */
    @Override
    public void handleBeforeEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        doScreenShot();
        throw throwable;
    }

    /**
     * Обрабатывает исключения, возникающие после выполнения каждого метода теста, делая скриншот перед повторным выбросом исключения.
     *
     * @param context   контекст теста
     * @param throwable возникшее исключение
     * @throws Throwable исключение, которое нужно повторно выбросить
     */
    @Override
    public void handleAfterEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        doScreenShot();
        throw throwable;
    }

    /**
     * Делает скриншот текущей страницы и прикрепляет его к Allure-отчету.
     */
    private void doScreenShot() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Allure.addAttachment(
                    "Screen on test end",
                    new ByteArrayInputStream(
                            Objects.requireNonNull(
                                    Selenide.screenshot(OutputType.BYTES)
                            )
                    )
            );
        }
    }

    /**
     * Настраивает Selenide перед запуском всех тестов.
     * Устанавливает размер окна браузера, таймауты, стратегию загрузки страниц.
     * Для Chrome отключает менеджер паролей и уведомления.
     *
     * @param context контекст теста
     */
    @Override
    public void beforeAll(ExtensionContext context) {
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 5000;
        Configuration.fastSetValue = true;
        Configuration.pageLoadStrategy = "normal";

        MutableCapabilities capabilities = new MutableCapabilities();

        if (WebDriverRunner.isChrome()) {
            ChromeOptions options = new ChromeOptions();
            var prefs = new HashMap<String, Object>();
            prefs.put("credentials_enable_service", false);
            prefs.put("password_manager_enabled", false);
            options.setExperimentalOption("prefs", prefs);
            options.addArguments("--disable-notifications");
            capabilities = capabilities.merge(options);
        }

        Configuration.browserCapabilities = capabilities;
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.closeWebDriver();
        }
    }

}