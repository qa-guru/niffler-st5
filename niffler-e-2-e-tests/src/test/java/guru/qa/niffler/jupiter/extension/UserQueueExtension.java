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

    /**
     * It's used to resolve a parameter in a test method, which is annotated with the @User annotation. The @User annotation specifies the type of user to be used in the test.
     * The purpose of the resolveParameter method in the context of JUnit testing frameworks is to dynamically resolve a value for a parameter in a test method by retrieving the corresponding dependency from the test's ApplicationContext. This method is part of the ParameterResolver interface, which defines the API for test extensions that wish to dynamically resolve parameters at runtime.
     * When a test class constructor, a test method, or a lifecycle method accepts a parameter, the parameter must be resolved at runtime by a registered ParameterResolver.
     * @param parameterContext the context for the parameter for which an argument should
     *                         be resolved; never {@code null}
     * @param extensionContext the extension context for the {@code Executable}
     *                         about to be invoked; never {@code null}
     * @return
     * @throws ParameterResolutionException
     */
    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        /*
        * Finding the Annotation:
        * The AnnotationSupport.findAnnotation() method is used to find the @User annotation on the parameter being resolved.
        * This annotation specifies the type of user to be used in the test.
        */
        Optional<User> annotation = AnnotationSupport.findAnnotation(parameterContext.getParameter(), User.class);

        /*
        * Getting the User Type:
        * The User.UserType enum value is retrieved from the annotation.
        * This enum value represents the type of user to be used in the test.
        */
        User.UserType userType = annotation.get().value();

        /*
        * Getting the Users from the Store:
        * The extensionContext.getStore() method is used to get a store that is specific to the current test extension context.
        * The store is used to cache the users of different types.
        * The get() method is used to retrieve the list of users of the specified type from the store.
        */
        Map<User.UserType, List<UserJson>> usersFromTest =
                extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);

        List<UserJson> usersOfType = usersFromTest.get(userType);

        /* This user is then used in the test */
        //todo - как написать код так, чтобы разрешать каждого из списка, если их несколько с одним типом,
        // например, в тесте withSameUserTypesTest()
        return usersOfType.getFirst();
    }
}