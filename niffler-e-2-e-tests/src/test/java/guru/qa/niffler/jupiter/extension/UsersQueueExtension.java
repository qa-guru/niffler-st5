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

// Любой тест проходит через него
public class UsersQueueExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UsersQueueExtension.class);

//    // todo Add new the functionality to send and accept invitations with API
//    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
//            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(BODY))
//            .build();
//
//    private final Retrofit retrofit = new Retrofit.Builder()
//            .client(okHttpClient)
//            .baseUrl("http://127.0.0.1:8089/")
//            .addConverterFactory(JacksonConverterFactory.create())
//            .build();

    private static final Queue<UserJson> FRIEND = new ConcurrentLinkedQueue<>();
    private static final Queue<UserJson> INVITE_SENT = new ConcurrentLinkedQueue<>();
    private static final Queue<UserJson> INVITE_RECEIVED = new ConcurrentLinkedQueue<>();

    static {
        FRIEND.add(userWithUsername("Alex"));
        FRIEND.add(userWithUsername("Shmel"));

        INVITE_SENT.add(userWithUsername("Shmel"));
        INVITE_SENT.add(userWithUsername("Alex"));

        INVITE_RECEIVED.add(userWithUsername("Alex"));
        INVITE_RECEIVED.add(userWithUsername("Aleksei"));
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        UserJson user = null;
        List<User> userAnnotations = Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, User.class))
                .map(p -> p.getAnnotation(User.class))
                .toList();

        for (User userAnnotation : userAnnotations) {
                switch (userAnnotation.selector()) {
                case FRIEND -> FRIEND.poll();
                case INVITE_SENT -> INVITE_SENT.poll();
                case INVITE_RECEIVED -> INVITE_RECEIVED.poll();
            };
            Allure.getLifecycle().updateTestCase(testCase -> {
                testCase.setStart(new Date().getTime());
            });
            context.getStore(NAMESPACE).put(userAnnotation.selector().name(), user);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        UserJson userFromTest = context.getStore(NAMESPACE).get("userFriend", UserJson.class);
        FRIEND.add(userFromTest);

        userFromTest = context.getStore(NAMESPACE).get("userInviteSent", UserJson.class);
        INVITE_SENT.add(userFromTest);

        userFromTest = context.getStore(NAMESPACE).get("userInviteReceived", UserJson.class);
        INVITE_RECEIVED.add(userFromTest);
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
            case FRIEND -> store.get("userFriend", UserJson.class);
            case INVITE_SENT -> store.get("userInviteSent", UserJson.class);
            case INVITE_RECEIVED ->
                    store.get("userInviteReceived", UserJson.class);
        };
    }
}
