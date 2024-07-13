package guru.qa.niffler.jupiter.extension;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public interface SuiteExtension extends AfterAllCallback {

    @Override
    default void afterAll(ExtensionContext context) throws Exception {
        context.getStore(ExtensionContext.Namespace.GLOBAL)
                .getOrComputeIfAbsent(this.getClass(), key -> {
                    beforeSuite(context);
                    return (ExtensionContext.Store.CloseableResource) this::afterSuite;
                });
    }

    default void beforeSuite(ExtensionContext extensionContext) {
    }

    default void afterSuite() {
    }

}
