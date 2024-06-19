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

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static guru.qa.niffler.model.UserJson.simpleUser;

// Любой тест проходит через него
public class    UserQueueExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UserQueueExtension.class);

    private static final Map<User.UserType, Queue<UserJson>> USERS = new ConcurrentHashMap<>();

    static {

        USERS.put(User.UserType.INVITATION_SEND, new ConcurrentLinkedQueue<>(List.of(
                simpleUser("Nastiletko", "bB!123456"))));
        USERS.put(User.UserType.WITH_FRIEND, new ConcurrentLinkedQueue<>(
                List.of(simpleUser("Nastiletko1", "bB!123456"),
                        simpleUser("Nastiletko2", "bB!123456"))));
        USERS.put(User.UserType.INVITATION_RECIEVED, new ConcurrentLinkedQueue<>(
                List.of(simpleUser("Nastiletko3", "bB!123456"))));
        USERS.put(User.UserType.DEFAULT_USER, new ConcurrentLinkedQueue<>(
                List.of(simpleUser("Nastiletko4", "bB!123456"))));
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


        List<User.UserType> userTypeList = methods.stream()
                .flatMap(m -> Arrays.stream(m.getParameters()))
                .filter(p -> p.isAnnotationPresent(User.class))
                .map(p -> p.getAnnotation(User.class).value())
                .toList();

        Map<User.UserType, List<UserJson>> users = new HashMap<>();

        for (User.UserType userType : userTypeList) {

            var currentQueue = USERS.get(userType);

            UserJson userForTest = null;
            while (userForTest == null) {
                userForTest = currentQueue.poll();
            }

            if (users.containsKey(userType)) {
                List<UserJson> userList = new ArrayList<>(users.get(userType));
                userList.add(userForTest);
                users.put(userType, userList);
            } else {
                users.put(userType, List.of(userForTest));
            }
        }

        Allure.getLifecycle().updateTestCase(testCase -> {
            testCase.setStart(new Date().getTime());
        });

        context.getStore(NAMESPACE).put(context.getUniqueId(), users);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        Map<User.UserType, List<UserJson>> usersFromTest =
                context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);

        usersFromTest.forEach((userType, userJsonList) ->
                USERS.get(userType).addAll(userJsonList)
        );
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
        User.UserType userType = parameterContext.getParameter().getAnnotation(User.class).value();
        Map<User.UserType, List<UserJson>> users = extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class);

        List<UserJson> userJsonList = users.get(userType);
        UserJson user = null;

        if (userJsonList != null && !userJsonList.isEmpty()) {
            user = userJsonList.getFirst();
            if (userJsonList.size() > 1) {
                userJsonList.remove(user);
                userJsonList.add(user);
            }
        }

        return user;
    }
}