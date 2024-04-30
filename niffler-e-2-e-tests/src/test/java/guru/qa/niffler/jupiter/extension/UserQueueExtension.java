package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static guru.qa.niffler.model.UserJson.simpleUser;

// Любой тест проходит через него
public class UserQueueExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UserQueueExtension.class);

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
    }

    @Override
    public void beforeEach(ExtensionContext context) {

        // список типов юзера для теста
        List<User> usersTypeList = Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, User.class))
                .map(p -> p.getAnnotation(User.class))
                .toList();

        // создаем списки для каждого типа пользователя
        Map<User.UserType, List<UserJson>> usersByType = new HashMap<>();
        usersTypeList.forEach(userType -> usersByType.computeIfAbsent(userType.value(), k -> new ArrayList<>()));

        // для каждого конкретного типа дожидаемся в очереди пользователя
        for (User userType : usersTypeList) {
            User.UserType selector = userType.value();

            // отрубит цикл через 30 секунд
            long fifteenSecondsLater = new Date().getTime() + 30000;

            UserJson userForTest = null;
            while (userForTest == null) {
                userForTest = switch (selector) {
                    case INVITATION_RECEIVED -> INVITATION_RECEIVED.poll();
                    case INVITATION_SEND -> INVITATION_SEND.poll();
                    case WITH_FRIENDS -> WITH_FRIENDS.poll();
                };
                // прерыватель по таймауту
                if (new Date().getTime() > fifteenSecondsLater)
                    throw new RuntimeException("Превышено время ожидания тестовых данных. Добавьте ресурсов!");
            }
            // дождавшись пользователя, добавляем его в типовой список
            usersByType.get(selector).add(userForTest);
        }

        // передаём Map с сортированными пользователями в контекст
        context.getStore(NAMESPACE).put(context.getUniqueId(), usersByType);

        Allure.getLifecycle().updateTestCase(testCase -> testCase.setStart(new Date().getTime()));
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        // забираем после теста Map с пользователями из контекста
        Map<User.UserType, List<UserJson>> usersFromTest =
                extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);

        if (usersFromTest != null) {
            // для каждого типа пользователя берём список пользователей
            usersFromTest.forEach((userType, userList) -> {
                // и возвращаем каждого типового пользователя в свою очередь
                userList.forEach(user -> {
                    switch (userType) {
                        case INVITATION_RECEIVED -> INVITATION_RECEIVED.add(user);
                        case INVITATION_SEND -> INVITATION_SEND.add(user);
                        case WITH_FRIENDS -> WITH_FRIENDS.add(user);
                    }
                });
            });
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class) &&
                parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Optional<User> annotation = AnnotationSupport.findAnnotation(parameterContext.getParameter(), User.class);
        User.UserType userType = annotation.get().value();

        Map<User.UserType, List<UserJson>> usersFromTest =
                extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);

        List<UserJson> usersOfType = usersFromTest.get(userType);

        return usersOfType.getFirst();
    }
}