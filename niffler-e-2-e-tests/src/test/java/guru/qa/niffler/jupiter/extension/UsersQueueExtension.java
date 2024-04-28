package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


import static guru.qa.niffler.model.UserJson.userWithUsername;

public class UsersQueueExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    private static final Queue<UserJson> FRIEND = new ConcurrentLinkedQueue<>();
    private static final Queue<UserJson> INVITE_SENT = new ConcurrentLinkedQueue<>();
    private static final Queue<UserJson> INVITE_RECEIVED = new ConcurrentLinkedQueue<>();

    static {
        FRIEND.add(userWithUsername("Alex"));
        FRIEND.add(userWithUsername("Shmel"));

        INVITE_SENT.add(userWithUsername("Anna"));
        INVITE_SENT.add(userWithUsername("Emilia"));

        INVITE_RECEIVED.add(userWithUsername("Ivan"));
        INVITE_RECEIVED.add(userWithUsername("Petr"));
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        UserJson userForTest = null;
        List<User> userAnnotations = Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, User.class))
                .map(p -> p.getAnnotation(User.class))
                .toList();

        for (User userAnnotation : userAnnotations) {
                switch (userAnnotation.selector()) {
                    case FRIEND:
                        userForTest = FRIEND.poll();
                        if (userForTest != null) {
                            context.getStore(NAMESPACE).put(context.getUniqueId() + "_userFriend", userForTest);
                        }
                        break;
                    case INVITE_SENT: userForTest = INVITE_SENT.poll();
                        if (userForTest != null) {
                            context.getStore(NAMESPACE).put(context.getUniqueId() + "_userInviteSent", userForTest);
                        }
                        break;
                    case INVITE_RECEIVED: userForTest = INVITE_RECEIVED.poll();
                        if (userForTest != null) {
                            context.getStore(NAMESPACE).put(context.getUniqueId() + "_userInviteReceived", userForTest);
                        }
                        break;
            };
            Allure.getLifecycle().updateTestCase(testCase -> {
                testCase.setStart(new Date().getTime());
            });
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        UserJson userFriend = context.getStore(NAMESPACE).remove(context.getUniqueId() + "_userFriend", UserJson.class);
        if (userFriend != null) {
            FRIEND.add(userFriend);
        }

        UserJson userInviteSent = context.getStore(NAMESPACE).remove(context.getUniqueId() + "_userInviteSent", UserJson.class);
        if (userInviteSent != null) {
            INVITE_SENT.add(userInviteSent);
        }

        UserJson userInviteReceived = context.getStore(NAMESPACE).remove(context.getUniqueId() + "_userInviteReceived", UserJson.class);
        if (userInviteReceived != null) {
            INVITE_RECEIVED.add(userInviteReceived);
        }
    }



    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.
                getParameter().
                getType().
                isAssignableFrom(
                        UserJson.class
                )
                &&
               parameterContext.
                       getParameter().
                       isAnnotationPresent(
                                User.class
                       );
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        User annotation = parameterContext.getParameter().getAnnotation(User.class);
        ExtensionContext.Store store = extensionContext.getStore(NAMESPACE);

        return switch (annotation.selector()) {
            case FRIEND -> store.get(extensionContext.getUniqueId() + "_userFriend", UserJson.class);
            case INVITE_SENT -> store.get(extensionContext.getUniqueId() + "_userInviteSent", UserJson.class);
            case INVITE_RECEIVED -> store.get(extensionContext.getUniqueId() + "_userInviteReceived", UserJson.class);
        };
    }
}
