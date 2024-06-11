package guru.qa.niffler.jupiter.extension;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public interface SuiteExtension extends AfterAllCallback {
    @Override
    default void afterAll(ExtensionContext extensionContext) throws Exception {
        extensionContext.getStore(ExtensionContext.Namespace.GLOBAL)
                .getOrComputeIfAbsent(this.getClass(), key -> {
                    beforeSuite(extensionContext);
                    return new ExtensionContext.Store.CloseableResource() {
                        @Override
                        public void close() throws Throwable {
                            afterSuite();
                        }
                    };
                });
    }

    default void beforeSuite(ExtensionContext extensionContext) {

    }

    default void afterSuite() {

    }
}
