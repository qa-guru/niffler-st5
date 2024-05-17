package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.CategoryApi;
import guru.qa.niffler.model.CategoryJson;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

public class HttpCategoryExtension extends AbstractCategoryExtension {

	private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
			.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(BODY))
			.build();

	private final Retrofit retrofit = new Retrofit.Builder()
			.client(okHttpClient)
			.baseUrl("http://127.0.0.1:8093/")
			.addConverterFactory(JacksonConverterFactory.create())
			.build();

	@Override
	protected CategoryJson createCategory(CategoryJson category) throws IOException {
		CategoryApi categoryApi = retrofit.create(CategoryApi.class);
		return categoryApi.createCategory(category).execute().body();
	}

	@Override
	protected void removeCategory(CategoryJson category) {

	}
}
