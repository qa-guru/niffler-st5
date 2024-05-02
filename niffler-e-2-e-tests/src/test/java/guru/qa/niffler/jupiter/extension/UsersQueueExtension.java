package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import static guru.qa.niffler.model.UserJson.userForTest;

public class UsersQueueExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    private static final Queue<UserJson> USERS_INVITE_SENT = new ConcurrentLinkedDeque<>();
    private static final Queue<UserJson> USERS_INVITE_RECEIVED = new ConcurrentLinkedDeque<>();
    private static final Queue<UserJson> USERS_FRIEND = new ConcurrentLinkedDeque<>();

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    static {
        USERS_FRIEND.add(userForTest("dotsarev", "dotsarev"));
        USERS_INVITE_SENT.add(userForTest("CAT", "CAT"));
        USERS_INVITE_RECEIVED.add(userForTest("DOG", "DOG"));
    }

    @Override
    public void beforeEach(ExtensionContext context) {

        List<User> usersTypeList = Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, User.class))
                .map(p -> p.getAnnotation(User.class))
                .toList();

        Map<User.Selector, List<UserJson>> usersByType = new HashMap<>();
        usersTypeList.forEach(userType -> usersByType.computeIfAbsent(userType.selector(), k -> new ArrayList<>()));

        for (User userType : usersTypeList) {
            User.Selector selector = userType.selector();

            UserJson userForTest = null;
            while (userForTest == null) {
                userForTest = switch (selector) {
                    case INVITE_RECEIVED -> USERS_INVITE_RECEIVED.poll();
                    case INVITE_SENT -> USERS_INVITE_SENT.poll();
                    case FRIEND -> USERS_FRIEND.poll();
                };
            }
            usersByType.get(selector).add(userForTest);
        }

        context.getStore(NAMESPACE).put(context.getUniqueId(), usersByType);

        Allure.getLifecycle().updateTestCase(testCase -> testCase.setStart(new Date().getTime()));
    }


    @Override
    public void afterEach(ExtensionContext extensionContext) {
        Map<User.Selector, List<UserJson>> usersFromTest =
                extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);

        if (usersFromTest != null) {
            usersFromTest.forEach((userType, userList) -> {
                userList.forEach(user -> {
                    switch (userType) {
                        case INVITE_RECEIVED -> USERS_INVITE_RECEIVED.add(user);
                        case INVITE_SENT -> USERS_INVITE_SENT.add(user);
                        case FRIEND -> USERS_FRIEND.add(user);
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

        User.Selector userType = annotation.get().selector();

        Map<User.Selector, List<UserJson>> users =
                extensionContext.getStore(NAMESPACE)
                        .get(extensionContext.getUniqueId(), Map.class);
        List<UserJson> userList = users.get(userType);

        return  userList.getFirst();
    }
}
