package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import org.junit.jupiter.api.extension.*;

public abstract class AbstractSpendExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

	protected abstract SpendEntity createSpend(SpendEntity spend);

	protected abstract void removeSpend(SpendEntity spend);

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {

	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {

	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return false;
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return null;
	}
}
