package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static guru.qa.niffler.model.UserJson.simpleUser;

public class UserQueueExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UserQueueExtension.class);

    private static final Map<User.UserType, Queue<UserJson>> usersQueue = new ConcurrentHashMap<>();

    private static final Queue<UserJson> INVITATION_SEND = new ConcurrentLinkedQueue<>();
    private static final Queue<UserJson> INVITATION_RECEIVED = new ConcurrentLinkedQueue<>();
    private static final Queue<UserJson> WITH_FRIENDS = new ConcurrentLinkedQueue<>();

    static {
        // Пререквизиты: созданы все пользователи ниже

        // Пользователи с FriendState.INVITE_SENT отправили всем запрос кроме FriendState.INVITE_SENT
        INVITATION_SEND.add(simpleUser("duck_send", "12345"));
        INVITATION_SEND.add(simpleUser("barsik_send", "12345"));

        // Пользователи с FriendState.INVITE_RECEIVED ничего не делали
        INVITATION_RECEIVED.add(simpleUser("duck_received", "12345"));
        INVITATION_RECEIVED.add(simpleUser("barsik_received", "12345"));

        // Пользователи с FriendState.FRIEND приняли запрос от пользователей с FriendState.INVITE_SENT
        WITH_FRIENDS.add(simpleUser("duck_friend", "12345"));
        WITH_FRIENDS.add(simpleUser("barsik_friend", "12345"));

        usersQueue.put(User.UserType.INVITATION_SEND, INVITATION_SEND);
        usersQueue.put(User.UserType.INVITATION_RECEIVED, INVITATION_RECEIVED);
        usersQueue.put(User.UserType.WITH_FRIENDS, WITH_FRIENDS);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        List<Parameter> parametersList = new ArrayList<>();
        Map<Parameter, UserJson> parameterUserJsonMap = new HashMap<>();

        // забрал параметры с beforeEach()
        Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
                .filter(m -> AnnotationSupport.isAnnotated(m, BeforeEach.class))
                .flatMap(m -> Arrays.stream(m.getParameters()))
                .filter(p -> AnnotationSupport.isAnnotated(p, User.class))
                .filter(p -> p.getType().isAssignableFrom(UserJson.class))
                .forEach(parametersList::add);

        // забрал параметры с тестового метода
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, User.class))
                .filter(p -> p.getType().isAssignableFrom(UserJson.class))
                .forEach(parametersList::add);
        // для каждого конкретного типа дожидаемся в очереди пользователя
        for (Parameter param : parametersList) {
            User.UserType selector = param.getAnnotation(User.class).value();
            Queue<UserJson> typedQueue = usersQueue.get(selector);

            // отрубит цикл через 30 секунд
            long endTime = new Date().getTime() + 30000;

            UserJson userForTest = null;
            while (userForTest == null) {
                userForTest = typedQueue.poll();
            }

            // наполняю мапу для контекста
            parameterUserJsonMap.put(param, userForTest);

            // прерыватель по таймауту
            if (new Date().getTime() > endTime)
                throw new RuntimeException("Превышено время ожидания тестовых данных. Добавьте ресурсов!");
        }

        // передаём мапу в контекст
        context.getStore(NAMESPACE).put(context.getUniqueId(), parameterUserJsonMap);
        Allure.getLifecycle().updateTestCase(testCase -> testCase.setStart(new Date().getTime()));
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        // забираем после теста мапу с пользователями из контекста
        Map<Parameter, UserJson> params =
                extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);

        // и возвращаем каждого пользователя в свою очередь
        params.forEach((param, userJson) -> usersQueue.get(param.getAnnotation(User.class).value()).add(userJson));
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class) &&
                parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    /**
     * It's used to resolve a parameter in a test method, which is annotated with the @User annotation. The @User annotation specifies the type of user to be used in the test.
     * The purpose of the resolveParameter method in the context of JUnit testing frameworks is to dynamically resolve a value for a parameter in a test method by retrieving the corresponding dependency from the test's ApplicationContext. This method is part of the ParameterResolver interface, which defines the API for test extensions that wish to dynamically resolve parameters at runtime.
     * When a test class constructor, a test method, or a lifecycle method accepts a parameter, the parameter must be resolved at runtime by a registered ParameterResolver.
     *
     * @param parameterContext the context for the parameter for which an argument should
     *                         be resolved; never {@code null}
     * @param extensionContext the extension context for the {@code Executable}
     *                         about to be invoked; never {@code null}
     */
    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Map<Parameter, UserJson> params =
                extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
        // резолвим пользователей по содержимому параметра
        return params.get(parameterContext.getParameter());
    }
}