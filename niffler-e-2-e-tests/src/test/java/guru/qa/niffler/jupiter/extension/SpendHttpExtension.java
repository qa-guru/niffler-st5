package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Objects;

public class SpendHttpExtension extends AbstractSpendExtension {
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://127.0.0.1:8093/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    @Override
    protected SpendJson createSpend(SpendJson spend) {
        SpendApi spendApi = retrofit.create(SpendApi.class);
        SpendJson newSpend;
        try {
            SpendJson result = spendApi.createSpend(spend).execute().body();
            newSpend = new SpendJson(
                    Objects.requireNonNull(result).id(),
                    result.spendDate(),
                    spend.categoryEntity(),
                    spend.currency(),
                    spend.amount(),
                    spend.description(),
                    spend.categoryEntity().getUsername(),
                    spend.categoryEntity().getCategory()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return newSpend;
    }

    @Override
    protected void removeSpend(SpendJson spend) {
        SpendApi spendApi = retrofit.create(SpendApi.class);
        try {
            spendApi.removeSpend(spend.username()).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
