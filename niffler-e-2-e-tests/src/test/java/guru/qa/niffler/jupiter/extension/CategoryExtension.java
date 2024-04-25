package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Objects;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

public class CategoryExtension implements BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(CategoryExtension.class);

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(BODY))
            .build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://127.0.0.1:8093/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        SpendApi spendApi = retrofit.create(SpendApi.class);

        AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                Category.class
        ).ifPresent(
                cat -> {
                    CategoryJson categoryJson = new CategoryJson(
                            null,
                            cat.category(),
                            cat.username()
                    );
                    try {
                        CategoryJson result = Objects.requireNonNull(
                                spendApi.createCategory(categoryJson).execute().body()
                        );
                        extensionContext.getStore(NAMESPACE).put(
                                extensionContext.getUniqueId(), result
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
