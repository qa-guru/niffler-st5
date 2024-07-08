package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.DbUser;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public abstract class AbstractCreateUserExtension implements BeforeEachCallback, ParameterResolver {
    // Создаем уникальное пространство имен для хранения данных в контексте расширения
    public static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(AbstractCreateUserExtension.class);

    // Метод, вызываемый перед каждым тестом
    @Override
    public void beforeEach(ExtensionContext context) {
        // Ищем аннотацию @DbUser у метода
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), DbUser.class)
                .ifPresent(testUser -> {
                    // Создаем случайного пользователя
                    UserJson user = UserJson.randomUser();
                    // Сохраняем пользователя в контексте расширения, используя уникальный идентификатор теста
                    context.getStore(NAMESPACE).put(context.getUniqueId(), createUser(user));
                });

        // Ищем аннотацию @ApiLogin у метода и аннотацию @DbUser у аннотации @ApiLogin
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), ApiLogin.class)
                .ifPresent(apiLogin -> {
                    if (apiLogin.user() != null) {
                        // Создаем случайного пользователя
                        UserJson user = UserJson.randomUser();
                        // Сохраняем пользователя в контексте расширения, используя уникальный идентификатор теста
                        context.getStore(NAMESPACE).put(context.getUniqueId(), createUser(user));
                    }
                });
    }

    // Метод, проверяющий, поддерживается ли параметр типа UserJson
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.
                getParameter().
                getType().
                isAssignableFrom(UserJson.class);
    }

    // Метод, разрешающий параметр типа UserJson
    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        // Получаем сохраненного пользователя из контекста расширения по уникальному идентификатору теста
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId());
    }

    // Абстрактный метод для создания пользователя, реализуемый в конкретных расширениях
    protected abstract UserJson createUser(UserJson user);

}

