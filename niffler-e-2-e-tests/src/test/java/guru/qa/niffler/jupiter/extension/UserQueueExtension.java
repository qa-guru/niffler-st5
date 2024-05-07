package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.UserType;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.reflect.Method;
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

    private static final Queue<UserJson> USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<UserJson> USERS_WITH_FRIEND = new ConcurrentLinkedQueue<>();
    private static final Queue<UserJson> USERS_WITH_FRIEND_2 = new ConcurrentLinkedQueue<>();
    private static final Queue<UserJson> USERS_WITH_INVITE = new ConcurrentLinkedQueue<>();

    static {

        USERS_WITH_INVITE.add(simpleUser("Nastiletko", "bB!123456",
                List.of("Nastiletko1")));
        USERS_WITH_FRIEND.add(simpleUser("Nastiletko1", "bB!123456",
                List.of("Nastiletko2")));
        USERS_WITH_FRIEND_2.add(simpleUser("Nastiletko2", "bB!123456",
                List.of("Nastiletko1")));
        USERS.add(simpleUser("Nastiletko3", "bB!123456", List.of()));

    }


    @Override
    public void beforeEach(ExtensionContext context) {

        Method testMethod = context.getRequiredTestMethod();

        List<Method> beforeEachMethods = Arrays.stream(
                        context.getRequiredTestClass().getDeclaredMethods())
                .filter(i -> i.isAnnotationPresent(BeforeEach.class)).toList();


        List<Method> methods = new ArrayList<>();
        methods.add(testMethod);
        methods.addAll(beforeEachMethods);


        List<UserType> userTypeList = methods.stream()
                .flatMap(m -> Arrays.stream(m.getParameters()))
                .filter(p -> p.isAnnotationPresent(User.class))
                .map(p -> p.getAnnotation(User.class).value())
                .toList();

        Map<UserType, UserJson> users = new HashMap<>();

        for (UserType userType : userTypeList) {

            UserJson userForTest = null;

            while (userForTest == null) {
                userForTest = switch (userType) {
                    case UserType.INVITATION_SEND -> USERS.poll();
                    case UserType.WITH_FRIEND -> USERS_WITH_FRIEND.poll();
                    case UserType.WITH_FRIEND_2 -> USERS_WITH_FRIEND_2.poll();
                    case UserType.INVITATION_RECIEVED -> USERS_WITH_INVITE.poll();
                };
            }
            users.put(userType, userForTest);
        }

        Allure.getLifecycle().updateTestCase(testCase -> {
            testCase.setStart(new Date().getTime());
        });

        context.getStore(NAMESPACE).put(context.getUniqueId(), users);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        Map<UserType, UserJson> usersFromTest =
                context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);

        for (Map.Entry<UserType, UserJson> user : usersFromTest.entrySet()) {
            switch (user.getKey()) {
                case UserType.INVITATION_SEND -> USERS.add(user.getValue());
                case UserType.WITH_FRIEND -> USERS_WITH_FRIEND.add(user.getValue());
                case UserType.WITH_FRIEND_2 -> USERS_WITH_FRIEND_2.add(user.getValue());
                case UserType.INVITATION_RECIEVED -> USERS_WITH_INVITE.add(user.getValue());
            }
        }

    }


    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(UserJson.class)
                && parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Optional<User> annotation = AnnotationSupport.findAnnotation(parameterContext.getParameter(), User.class);
        UserType userType = annotation.get().value();
        Map<UserType, UserJson> users = extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class);

        return users.get(userType);
    }
}