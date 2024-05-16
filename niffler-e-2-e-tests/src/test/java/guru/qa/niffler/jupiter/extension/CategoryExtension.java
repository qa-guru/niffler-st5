package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.model.CategoryJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

import static java.util.Objects.requireNonNull;
import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

public class CategoryExtension implements BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://127.0.0.1:8093/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        findAnnotation(
                extensionContext.getRequiredTestMethod(),
                GenerateCategory.class
        ).ifPresent(
                generateCategory -> {
                    CategoryJson categoryJson = new CategoryJson(
                            null,
                            generateCategory.category(),
                            generateCategory.username()
                    );

                    try {
                        CategoryJson result = requireNonNull(spendApi.getCategories(categoryJson.username()).execute()
                                .body()).stream()
                                .filter(cat -> cat.category().equals(generateCategory.category()))
                                .findAny()
                                .orElse(spendApi.createCategory(categoryJson).execute().body());

                        extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), result);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

}
