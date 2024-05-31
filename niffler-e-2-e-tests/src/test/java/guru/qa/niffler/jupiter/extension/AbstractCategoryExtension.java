package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.io.IOException;

public abstract class AbstractCategoryExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

	public static final ExtensionContext.Namespace NAMESPACE
			= ExtensionContext.Namespace.create(AbstractCategoryExtension.class);

	protected abstract CategoryJson createCategory(CategoryJson category) throws IOException;

	protected abstract void removeCategory(CategoryJson category);

	@Override
	public void beforeEach(ExtensionContext extensionContext) {

		UserJson user = extensionContext.getStore(CreateUserExtension.NAMESPACE)
				.get(extensionContext.getUniqueId(), UserJson.class);

		AnnotationSupport.findAnnotation(
				extensionContext.getRequiredTestMethod(),
				GenerateCategory.class
		).ifPresent(
				category -> {
					CategoryJson categoryJson = CategoryJson.randomByUsername(user.username());
					try {
						extensionContext.
								getStore(NAMESPACE).
								put(extensionContext.getUniqueId(), createCategory(categoryJson));
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				});
	}

	@Override
	public void afterEach(ExtensionContext context) {
		removeCategory(context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class));
	}

	@Override
	public boolean supportsParameter(
			ParameterContext parameterContext,
			ExtensionContext extensionContext
	) throws ParameterResolutionException {
		return parameterContext
				.getParameter()
				.getType()
				.isAssignableFrom(CategoryJson.class);
	}

	@Override
	public CategoryJson resolveParameter(
			ParameterContext parameterContext,
			ExtensionContext extensionContext
	) throws ParameterResolutionException {
		return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
	}
}
