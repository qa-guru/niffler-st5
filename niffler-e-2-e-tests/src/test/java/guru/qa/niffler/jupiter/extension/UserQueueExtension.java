package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static guru.qa.niffler.jupiter.annotation.User.UserType.*;


public class UserQueueExtension implements
		BeforeEachCallback,
		AfterEachCallback,
		ParameterResolver {

	public static final ExtensionContext.Namespace NAMESPACE
			= ExtensionContext.Namespace.create(UserQueueExtension.class);

	private static final Map<User.UserType, Queue<UserJson>> USERS = new ConcurrentHashMap<>();

	static {
		USERS
				.put(
						INVITE_SENT,
						new ConcurrentLinkedQueue<>(List.of(
								UserJson.simpleUser("petrov", "12345"),
								UserJson.simpleUser("smirnov", "12345"))));
		USERS
				.put(
						INVITE_RECEIVED,
						new ConcurrentLinkedQueue<>(List.of(
								UserJson.simpleUser("ivanov", "12345"),
								UserJson.simpleUser("sidorov", "12345"))));
		USERS
				.put(
						FRIEND,
						new ConcurrentLinkedQueue<>(List.of(
								UserJson.simpleUser("demidov", "123456"),
								UserJson.simpleUser("manulov", "12345"))));
	}


	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		List<User> usersTypeList = Arrays.stream(context.getRequiredTestMethod().getParameters())
				.filter(p -> AnnotationSupport.isAnnotated(p, User.class))
				.map(p -> p.getAnnotation(User.class))
				.toList();

		Map<User.UserType, List<UserJson>> users = new HashMap<>();

		for (User type : usersTypeList) {
			Queue<UserJson> queueUsers = USERS.get(type.userType());
			UserJson userJson = null;
			while (userJson == null) {
				userJson = queueUsers.poll();
			}
			if (users.get(type.userType()) == null) {
				users.put(type.userType(), List.of(userJson));
			} else {
				List<UserJson> listUser = List.of(users.get(type.userType()).getFirst(), userJson);
				users.remove(type.userType());
				users.put(type.userType(), listUser);
			}
		}
		Allure.getLifecycle().updateTestCase(testCase ->
				testCase.setStart(new Date().getTime()));
		context.getStore(NAMESPACE).put(context.getUniqueId(), users);
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		Map<User.UserType, List<UserJson>> usersTest = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
		for (Map.Entry<User.UserType, List<UserJson>> user : usersTest.entrySet()) {
			USERS.get(user.getKey()).addAll(user.getValue());
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
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		Optional<User> annotation = AnnotationSupport.findAnnotation(parameterContext.getParameter(), User.class);
		User.UserType userUserType = annotation.get().userType();
		Map<User.UserType, List<UserJson>> users = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
		if (users.size() == 1 && users.get(userUserType).size() == 1) {
			return users.get(userUserType).getFirst();
		} else {
			return parameterContext.getIndex() == 0 ? users.get(userUserType).getFirst() : users.get(userUserType).getLast();
		}
	}
}

