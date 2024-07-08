package guru.qa.niffler.jupiter.extension;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Расширение JUnit Jupiter для управления контекстом теста.
 * Сохраняет контекст теста в ThreadLocal для доступа из любого места.
 */
public class ContextHolderExtension implements BeforeEachCallback, AfterEachCallback {

    /**
     * Сохраняет контекст теста перед каждым тестовым методом.
     *
     * @param context контекст теста
     * @throws Exception если возникает ошибка при сохранении контекста
     */
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Holder.INSTANCE.set(context);
    }

    /**
     * Удаляет контекст теста после каждого тестового метода.
     *
     * @param context контекст теста
     * @throws Exception если возникает ошибка при удалении контекста
     */
    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Holder.INSTANCE.remove();
    }

    /**
     * Возвращает текущий контекст теста.
     *
     * @return контекст теста
     */
    public static ExtensionContext context() {
        return Holder.INSTANCE.get();
    }

    /**
     * Внутренний класс-держатель для потокобезопасного хранения контекста теста.
     */
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
