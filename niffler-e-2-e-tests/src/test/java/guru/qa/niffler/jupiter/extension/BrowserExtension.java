package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Objects;

public class BrowserExtension implements TestExecutionExceptionHandler,
        BeforeAllCallback,
        AfterEachCallback,
        LifecycleMethodExecutionExceptionHandler {

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        doScreenShot();
        throw throwable;
    }

    @Override
    public void handleBeforeEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        doScreenShot();
        throw throwable;
    }

    @Override
    public void handleAfterEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        doScreenShot();
        throw throwable;
    }

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