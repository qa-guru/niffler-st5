package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Date;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

public class HttpSpendExtension extends AbstractSpendExtension {
	private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
			.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(BODY))
			.build();

	private final Retrofit retrofit = new Retrofit.Builder()
			.client(okHttpClient)
			.baseUrl("http://127.0.0.1:8093/")
			.addConverterFactory(JacksonConverterFactory.create())
			.build();

	@Override
	protected SpendJson createSpend(GenerateSpend spend, CategoryJson categoryJson) throws IOException {
		SpendApi spendApi = retrofit.create(SpendApi.class);
		SpendJson spendJson = new SpendJson(
				null,
				new Date(),
				categoryJson.category(),
				spend.currency(),
				spend.amount(),
				spend.description(),
				categoryJson.username());
		return spendApi.createSpend(spendJson).execute().body();
	}

	@Override
	protected void removeSpend(SpendEntity spend) {

	}
}
