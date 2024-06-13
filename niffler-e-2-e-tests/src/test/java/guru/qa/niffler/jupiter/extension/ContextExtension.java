package guru.qa.niffler.jupiter.extension;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ContextExtension implements BeforeEachCallback, AfterEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Holder.INSTANCE.set(context);
    }


    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Holder.INSTANCE.remove();
    }


    public static ExtensionContext context() {
        return Holder.INSTANCE.get();
    }

    private enum Holder {
        INSTANCE;

        ThreadLocal<ExtensionContext> store = new ThreadLocal<>();

        void set(ExtensionContext context) {
            store.set(context);
        }

        ExtensionContext get() {
            return store.get();
        }

        void remove() {
            store.remove();
        }
    }
}
