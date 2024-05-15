package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public abstract class AbstractCategoryExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

	public static final ExtensionContext.Namespace NAMESPACE
			= ExtensionContext.Namespace.create(AbstractCategoryExtension.class);

	protected abstract CategoryJson createCategory(CategoryJson category);

	protected abstract void removeCategory(CategoryJson category);

	@Override
	public void beforeEach(ExtensionContext extensionContext) throws Exception {
		AnnotationSupport.findAnnotation(
				extensionContext.getRequiredTestMethod(),
				GenerateCategory.class
		).ifPresent(
				category -> {
					CategoryJson categoryJson = new CategoryJson(
							null,
							category.category(),
							category.username()
					);
					extensionContext.
							getStore(NAMESPACE).
							put(extensionContext.getUniqueId(), createCategory(categoryJson));
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
