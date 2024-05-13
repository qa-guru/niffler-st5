package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.CategoryApi;
import guru.qa.niffler.model.CategoryJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.ExtensionContext;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

public class CategoryHttpExtension extends AbstractCategoryExtention{
    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(CategoryHttpExtension.class);

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://127.0.0.1:8093/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    @Override
    protected CategoryJson createCategory(CategoryJson category) {
        CategoryApi categoryApi = retrofit.create(CategoryApi.class);
        CategoryJson result;
        try {
            result = categoryApi.createCategory(category).execute().body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        return result;
    }

    @Override
    protected void removeCategory(CategoryJson category) {

    }
}
