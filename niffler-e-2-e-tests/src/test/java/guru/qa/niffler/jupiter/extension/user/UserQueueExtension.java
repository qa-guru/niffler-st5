package guru.qa.niffler.jupiter.extension.user;

import guru.qa.niffler.constant.Friendship;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static guru.qa.niffler.constant.Friendship.DEFAULT;
import static guru.qa.niffler.constant.Friendship.INVITATION_RECEIVED;
import static guru.qa.niffler.constant.Friendship.PENDING_INVITATION;
import static guru.qa.niffler.constant.Friendship.WITH_FRIENDS;
import static guru.qa.niffler.model.UserJson.simpleUser;
import static java.util.Arrays.stream;
import static org.junit.platform.commons.support.AnnotationSupport.isAnnotated;


public class UserQueueExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserQueueExtension.class);

    private static final Map<Friendship, ConcurrentLinkedQueue<UserJson>> USERS = new ConcurrentHashMap<>();

    static {
        USERS.put(WITH_FRIENDS, new ConcurrentLinkedQueue<>());
        USERS.put(INVITATION_RECEIVED, new ConcurrentLinkedQueue<>());
        USERS.put(PENDING_INVITATION, new ConcurrentLinkedQueue<>());
        USERS.put(DEFAULT, new ConcurrentLinkedQueue<>());

        USERS.get(WITH_FRIENDS).add(simpleUser("zhanna1", "test"));
        USERS.get(WITH_FRIENDS).add(simpleUser("friend", "friend111"));
        USERS.get(INVITATION_RECEIVED).add(simpleUser("dima", "test_user1"));
        USERS.get(PENDING_INVITATION).add(simpleUser("duck", "krya-krya2"));
        USERS.get(DEFAULT).add(simpleUser("tester", "qa_pass_666"));
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        Map<Friendship, List<UserJson>> testUsers = new HashMap<>();

        List<Friendship> beforeEachUserFriendships = stream(
                context.getRequiredTestClass().getDeclaredMethods())
                .filter(method -> (method.isAnnotationPresent(BeforeEach.class)))
                .flatMap(method -> stream(method.getParameters())
                        .filter(parameter -> parameter.isAnnotationPresent(User.class))
                        .map(parameter -> parameter.getAnnotation(User.class).friendship()))
                .toList();

        List<Friendship> testMethodUserFriendships = stream(
                context.getRequiredTestMethod().getParameters())
                .filter(parameter -> isAnnotated(parameter, User.class))
                .map(parameter -> parameter.getAnnotation(User.class).friendship())
                .toList();

        List<Friendship> friendships = new ArrayList<>();
        friendships.addAll(beforeEachUserFriendships);
        friendships.addAll(testMethodUserFriendships);
        friendships.forEach(annotation -> testUsers.put(annotation, new ArrayList<>()));

        for (Friendship friendship : friendships) {
            UserJson user = null;
            while (user == null) {
                user = USERS.get(friendship).poll();
            }
            testUsers.get(friendship).add(user);
        }

        Allure.getLifecycle() // В Timeline видно, что тесты выполняются друг за другом - не одновременно
                .updateTestCase(
                        testCase -> testCase.setStart(new Date().getTime())
                );

        context.getStore(NAMESPACE).put(context.getUniqueId(), testUsers);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterEach(ExtensionContext context) {
        Map<Friendship, List<UserJson>> testUsers = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        if (testUsers != null) testUsers.forEach((key, value) -> USERS.get(key).addAll(value));
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.isAnnotated(User.class) && parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Map<Friendship, List<UserJson>> testUserMap = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
        Friendship friendship = parameterContext.getParameter().getAnnotation(User.class).friendship();

        List<UserJson> testUserList = testUserMap.get(friendship);
        UserJson user = testUserList.getFirst();
        if (testUserList.size() > 1) Collections.rotate(testUserList, 1);

        return user;
    }

}