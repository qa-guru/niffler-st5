package guru.qa.niffler.jupiter.extension;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Интерфейс расширяет функциональность JUnit5 для работы с тестовыми наборами.
 * Основная идея этого интерфейса - предоставить механизм для выполнения дополнительных действий до и после выполнения всех тестов в наборе.
 * Это может быть полезно для инициализации или очистки ресурсов, связанных с тестированием.
 */
public interface SuiteExtension extends AfterAllCallback {

    /**
     * Переопределяет afterAll() из интерфейса AfterAllCallback из JUnit Jupiter.
     * Вызывается после завершения выполнения всех тестов в наборе.
     * @param extensionContext the current extension context; never {@code null}
     * @throws Exception
     */
    @Override
    default void afterAll(ExtensionContext extensionContext) throws Exception {
        extensionContext.getStore(ExtensionContext.Namespace.GLOBAL) // Получение глобального хранилища из контекста расширения
                .getOrComputeIfAbsent(this.getClass(), key -> { // this.getClass() вернёт класс имплементации, в котором будет вызван этот код
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
