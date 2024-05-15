package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Date;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

public class SpendExtension implements BeforeEachCallback, ParameterResolver {

	public static final ExtensionContext.Namespace NAMESPACE
			= ExtensionContext.Namespace.create(SpendExtension.class);

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

		CategoryJson category = extensionContext.getStore(CategoryExtension.NAMESPACE).get("category", CategoryJson.class);

		AnnotationSupport.findAnnotation(
				extensionContext.getRequiredTestMethod(),
				GenerateSpend.class
		).ifPresent(
				generateSpend -> {
					SpendJson spendJson = new SpendJson(
							null,
							new Date(),
							category.category(),
							generateSpend.currency(),
							generateSpend.amount(),
							generateSpend.description(),
							category.username()
					);
					try {
						SpendJson result = spendApi.createSpend(spendJson).execute().body();
						extensionContext.getStore(NAMESPACE).put(
								extensionContext.getUniqueId(),
								result
						);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
		);
	}

	@Override
	public boolean supportsParameter(
			ParameterContext parameterContext,
			ExtensionContext extensionContext
	) throws ParameterResolutionException {
		return parameterContext
				.getParameter()
				.getType()
				.isAssignableFrom(SpendJson.class);
	}

	@Override
	public Object resolveParameter(
			ParameterContext parameterContext,
			ExtensionContext extensionContext
	) throws ParameterResolutionException {
		return CategoryJson.fromEntity(extensionContext.getStore(NAMESPACE).
				get(extensionContext.getUniqueId(), CategoryEntity.class));
	}
}
