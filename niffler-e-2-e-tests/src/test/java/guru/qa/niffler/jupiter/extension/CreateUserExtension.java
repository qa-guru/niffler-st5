package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public abstract class CreateUserExtension implements BeforeEachCallback, ParameterResolver {
	abstract UserJson createUser(UserJson user);

	public static final ExtensionContext.Namespace NAMESPACE
			= ExtensionContext.Namespace.create(CreateUserExtension.class);

	@Override
	public void beforeEach(ExtensionContext context) {
		AnnotationSupport.findAnnotation(
				context.getRequiredTestMethod(),
				TestUser.class
		).ifPresent(testUser -> {
			context.getStore(NAMESPACE).put(context.getUniqueId(), createUser(UserJson.randomUser()));
		});
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return parameterContext
				.getParameter()
				.getType()
				.isAssignableFrom(UserJson.class);
	}

	@Override
	public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), UserJson.class);
	}
}
