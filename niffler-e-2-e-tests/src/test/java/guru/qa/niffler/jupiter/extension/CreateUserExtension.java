package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.annotation.TestUsers;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CreateUserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace CREATE_USER_NAMESPACE
            = ExtensionContext.Namespace.create(CreateUserExtension.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Map<User.Point, List<TestUser>> usersForTest = extractUsersForTest(extensionContext);

        Map<User.Point, List<UserJson>> createdUsers = new HashMap<>();
        for (Map.Entry<User.Point, List<TestUser>> userInfo : usersForTest.entrySet()) {
            List<UserJson> usersForPoint = new ArrayList<>();
            for (TestUser testUser : userInfo.getValue()) {
                usersForPoint.add(createUser(testUser));
            }
            createdUsers.put(userInfo.getKey(), usersForPoint);
        }

        extensionContext.getStore(CREATE_USER_NAMESPACE).put(extensionContext.getUniqueId(), createdUsers);
    }

    public abstract UserJson createUser(TestUser user);

    public abstract UserJson createCategory(TestUser user, UserJson createdUser);

    public abstract UserJson createSpend(TestUser user, UserJson createdUser);

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return AnnotationSupport.findAnnotation(parameterContext.getParameter(), User.class).isPresent() &&
                (parameterContext.getParameter().getType().isAssignableFrom(UserJson.class) ||
                        parameterContext.getParameter().getType().isAssignableFrom(UserJson[].class));
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        User user = AnnotationSupport.findAnnotation(parameterContext.getParameter(), User.class).get();
        Map<User.Point, List<UserJson>> createdUsers = extensionContext.getStore(CREATE_USER_NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
        List<UserJson> userJsons = createdUsers.get(user.value());
        if (parameterContext.getParameter().getType().isAssignableFrom(UserJson[].class)) {
            return userJsons.stream().toList().toArray(new UserJson[0]);
        } else {
            return userJsons.getFirst();
        }
    }

    private Map<User.Point, List<TestUser>> extractUsersForTest(ExtensionContext context) {
        Map<User.Point, List<TestUser>> result = new HashMap<>();
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), ApiLogin.class).ifPresent(
                apiLogin -> {
                    TestUser user = apiLogin.user();
                    if (!user.fake()) {
                        result.put(User.Point.INNER, List.of(user));
                    }
                }
        );
        List<TestUser> outerUsers = new ArrayList<>();
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), TestUser.class).ifPresent(
                tu -> {
                    if (!tu.fake()) outerUsers.add(tu);
                }
        );
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), TestUsers.class).ifPresent(
                testUsers -> Arrays.stream(testUsers.value())
                        .filter(tu -> !tu.fake())
                        .forEach(outerUsers::add)
        );
        result.put(User.Point.OUTER, outerUsers);
        return result;
    }

}
