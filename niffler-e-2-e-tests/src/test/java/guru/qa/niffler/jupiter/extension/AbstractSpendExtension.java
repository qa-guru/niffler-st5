package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Date;

public abstract class AbstractSpendExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(AbstractSpendExtension.class);

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://127.0.0.1:8093/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        CategoryJson category = extensionContext.getStore(AbstractCategoryExtension.NAMESPACE)
                .get(extensionContext.getUniqueId(), CategoryJson.class);

        AnnotationSupport.findAnnotation(
                        extensionContext.getRequiredTestMethod(),
                        GenerateSpend.class)
                .ifPresent(
                        spend -> {
                            SpendJson spendJson = new SpendJson(
                                    null,
                                    new Date(),
                                    category.category(),
                                    spend.currency(),
                                    spend.amount(),
                                    spend.description(),
                                    category.username()
                            );
                            try {
                                extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), createSpend(spendJson));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
    }

    @Override
    public void afterEach(ExtensionContext context) {
        SpendJson json = context.getStore(NAMESPACE).get(context.getUniqueId(), SpendJson.class);
        if (json != null) {
            removeSpend(json);
        }
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

    protected abstract SpendJson createSpend(SpendJson spend) throws Exception;


    protected abstract void removeSpend(SpendJson json);
}
