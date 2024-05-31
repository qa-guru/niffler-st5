package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.io.IOException;

public abstract class AbstractSpendExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

	public static final ExtensionContext.Namespace NAMESPACE
			= ExtensionContext.Namespace.create(AbstractSpendExtension.class);

	protected abstract Object createSpend(SpendEntity spendJson) throws IOException;

	protected abstract void removeSpend(SpendEntity spend);

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {

		CategoryJson category = context.getStore(AbstractCategoryExtension.NAMESPACE)
				.get(context.getUniqueId(), CategoryJson.class);

		AnnotationSupport.findAnnotation(
				context.getRequiredTestMethod(),
				GenerateSpend.class
		).ifPresent(
				generateSpend -> {
					SpendEntity randomSpend = SpendEntity.randomByCategory(category);
					try {
						context.getStore(NAMESPACE).put(
								context.getUniqueId(),
								createSpend(randomSpend)
						);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}

				}
		);
	}

	@Override
	public void afterEach(ExtensionContext context) {
		removeSpend(context.getStore(NAMESPACE).get(context.getUniqueId(), SpendEntity.class));
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return parameterContext
				.getParameter()
				.getType()
				.isAssignableFrom(SpendJson.class);
	}

	@Override
	public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return SpendJson.fromEntity(extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), SpendEntity.class));
	}
}
