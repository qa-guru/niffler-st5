package guru.qa.niffler.extensions;

import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.retrofit.spendsEndpoint.SpendsClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class NewSpendExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(NewSpendExtension.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) {

        CategoryJson category = extensionContext.getStore(CategoriesExtension.NAMESPACE).get(
                extensionContext.getUniqueId(),
                CategoryJson.class
        );
        AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                Spend.class
        ).ifPresent(
                spend -> {
                    var res = new SpendsClient()
                            .createSpend(category.category(), spend.currency(), spend.amount(), spend.description(), category.username());
                    extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), res);
                }
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(SpendJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId());
    }
}
