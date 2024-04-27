package guru.qa.niffler.extensions;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.retrofit.categoriesEndpoint.CategoriesClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoriesExtension implements BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(CategoriesExtension.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {

        AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                Category.class
        ).ifPresent(
                category -> {
                    var cat = new CategoriesClient()
                            .addNewCategory(category.category(), category.username());
                    extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), cat);
                });
    }

}


//    @Override
//    public void beforeEach(ExtensionContext extensionContext) throws Exception {
//        SpendJson testData = resolveParameter(null, extensionContext);
//        new CategoriesClient().addNewCategory(testData.category(), testData.username());
//        new SpendsClient().createSpend(testData.category(), testData.currency(), testData.amount(), testData.description(), testData.username());
//    }
//
//    @Override
//    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
//        return parameterContext.getParameter().getType().equals(SpendJson.class);
//    }
//
//    @Override
//    public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
//        TestData testData = extensionContext.getRequiredTestMethod().getAnnotation(TestData.class);
//        return new SpendJson(UUID.randomUUID(), new Date(), testData.categoryName(), testData.currency(), testData.amount(), testData.comment(), testData.userName());
//    }


//    @Override
//    public void beforeEach(ExtensionContext extensionContext) throws Exception {
//        new CategoriesClient().addNewCategory("Аквадискотека", "dima");
//        new SpendsClient().createSpend("Аквадискотека", CurrencyValues.RUB, 65000.00, "хорошо поплавали", "dima");
//    }

