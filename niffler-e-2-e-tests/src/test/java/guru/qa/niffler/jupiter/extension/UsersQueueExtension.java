package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static guru.qa.niffler.jupiter.annotation.User.Selector.*;
import static guru.qa.niffler.model.UserJson.simpleUser;

public class UsersQueueExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    private static final Map<User.Selector, Queue<UserJson>> USERS = new ConcurrentHashMap<>();

    static {
        USERS.put(INVITATION_SENT, new ConcurrentLinkedQueue<>(
                List.of(
                        simpleUser("testuser1", "Voisjf%05842"),
                        simpleUser("testuser11", "Voisjf%05842"),
                        simpleUser("testuser12", "Voisjf%05842")
                ))
        );

        USERS.put(INVITATION_RECEIVED, new ConcurrentLinkedQueue<>(
                List.of(
                        simpleUser("testuser2", "Voisjf%05842"),
                        simpleUser("testuser21", "Voisjf%05842"),
                        simpleUser("testuser22", "Voisjf%05842")
                ))
        );

        USERS.put(WITH_FRIENDS, new ConcurrentLinkedQueue<>(
                List.of(
                        simpleUser("testuser3", "Voisjf%05842"),
                        simpleUser("testuser31", "Voisjf%05842"),
                        simpleUser("testuser32", "Voisjf%05842")
                ))
        );

        USERS.put(ACCEPTED_FRIENDS, new ConcurrentLinkedQueue<>(
                List.of(
                        simpleUser("testuser4", "Voisjf%05842"),
                        simpleUser("testuser41", "Voisjf%05842"),
                        simpleUser("testuser42", "Voisjf%05842")
                ))
        );
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        List<Method> methods = new ArrayList<>();
        methods.add(context.getRequiredTestMethod());
        Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(BeforeEach.class))
                .forEach(methods::add);

        List<Parameter> parameters = methods.stream()
                .map(Executable::getParameters)
                .flatMap(Arrays::stream)
                .filter(parameter -> parameter.isAnnotationPresent(User.class))
                .filter(parameter -> parameter.getType().isAssignableFrom(UserJson.class))
                .toList();

        Map<User.Selector, UserJson> usersForTest = new HashMap<>();


        for (Parameter parameter : parameters) {
            User.Selector selector = parameter.getAnnotation(User.class).selector();

            if (usersForTest.containsKey(selector)) {
                continue;
            }
            UserJson testCandidate = null;
            Queue<UserJson> queue = USERS.get(selector);
            while (testCandidate == null) {
                testCandidate = queue.poll();
            }
            usersForTest.put(selector, testCandidate);
        }
        context.getStore(NAMESPACE)
                .put(context.getUniqueId(), usersForTest);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        Map<User.Selector, UserJson> usersFromTest = (Map<User.Selector, UserJson>) context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        for (User.Selector selector : usersFromTest.keySet()) {
            USERS.get(selector).add(usersFromTest.get(selector));
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(UserJson.class)
                &&
                parameterContext
                        .getParameter()
                        .isAnnotationPresent(User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (UserJson) extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class)
                .get(parameterContext.findAnnotation(User.class)
                        .get()
                        .selector());
    }
}
