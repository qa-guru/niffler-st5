package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.extension.ExtensionContext;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Objects;

import static guru.qa.niffler.utils.DateHelper.convertStringToDate;
import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

public class SpendHttpExtension extends AbstractSpendExtension {

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(BODY))
            .build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://127.0.0.1:8093/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    @Override
    protected SpendJson createSpend(ExtensionContext extensionContext, Spend spend) {
        SpendApi spendApi = retrofit.create(SpendApi.class);

        CategoryJson category = extensionContext.getStore(CategoryHttpExtension.NAMESPACE).get(
                extensionContext.getUniqueId(),
                CategoryJson.class);

        SpendJson spendJson = new SpendJson(
                null,
                convertStringToDate(spend.spendDate()),
                spend.category(),
                spend.currency(),
                spend.amount(),
                spend.description(),
                category.username()
        );

        try {
            return Objects.requireNonNull(
                    spendApi.createSpend(spendJson).execute().body()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void removeSpend(SpendJson spend) {
    }
}
