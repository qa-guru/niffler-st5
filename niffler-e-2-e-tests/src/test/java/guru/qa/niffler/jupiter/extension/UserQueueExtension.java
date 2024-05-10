package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

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

    private static final Map<User.UserType, Queue<UserJson>> USERS = new ConcurrentHashMap<>();

    static {
        USERS.put(User.UserType.WITH_FRIENDS, new ConcurrentLinkedQueue<>(List.of(simpleUser("DIMA", "12345"))));

        USERS.put(User.UserType.INVITATION_RECEIVED, new ConcurrentLinkedQueue<>(List.of(simpleUser("korova", "12345"))));

        USERS.put(User.UserType.INVITATION_SEND, new ConcurrentLinkedQueue<>(List.of(simpleUser("zayac", "12345"))));

    }


    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        List<User.UserType> allParameters = new ArrayList<>();

        List<User.UserType> beforeEachParameters = Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
                .filter(p -> p.isAnnotationPresent(BeforeEach.class)).map(p -> p.getAnnotation(User.class).value()).toList();

        List<User.UserType> testParameters =
                Arrays.stream(context.getRequiredTestMethod().getParameters())
                        .filter(p -> AnnotationSupport.isAnnotated(p, User.class))
                        .map(p -> p.getAnnotation(User.class).value()).toList();

        allParameters.addAll(beforeEachParameters);
        allParameters.addAll(testParameters);

        Map<User.UserType, List<UserJson>> users = new HashMap<>();

        for (User.UserType type : allParameters) {

            Queue<UserJson> queue = USERS.get(type);

            UserJson userForTest = null;

            while (userForTest == null) {
                userForTest = queue.poll();
            }

            users.put(type, List.of(userForTest));

        }

        Allure.getLifecycle().updateTestCase(testCase -> {
            testCase.setStart(new Date().getTime());
        });

        context.getStore(NAMESPACE).put(context.getUniqueId(), users);
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        Map<User.UserType, List<UserJson>> usersFromTest =
                extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);

        for (Map.Entry<User.UserType, List<UserJson>> users : usersFromTest.entrySet()) {
            USERS.get(users.getKey()).addAll(users.getValue());
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
        User.UserType userType = annotation.get().value();

        Map<User.UserType, List<UserJson>> users = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);

        return users.get(userType).getFirst();
    }

}
