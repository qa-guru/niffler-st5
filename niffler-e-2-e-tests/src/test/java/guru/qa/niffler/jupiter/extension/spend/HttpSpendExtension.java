package guru.qa.niffler.jupiter.extension.spend;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

public class HttpSpendExtension extends AbstractSpendExtension {

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://127.0.0.1:8093/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @Override
    protected SpendJson createSpend(SpendJson spendJson) {
        try {
            return spendApi.createSpend(spendJson).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void removeSpend(SpendJson spendJson) {
        try {
            spendApi.deleteSpends(spendJson.username(), List.of(spendJson.id())).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}