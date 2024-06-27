package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

public abstract class ApiClient {

    private final OkHttpClient okHttpClient;
    protected final Retrofit retrofit;

    protected static final Config CONFIG = Config.getInstance();

    public ApiClient(String baseUrl) {
        this(baseUrl, false, JacksonConverterFactory.create(), BODY);
    }

    public ApiClient(String baseUrl, boolean followRedirect) {
        this(baseUrl, followRedirect, JacksonConverterFactory.create(), BODY);
    }

    public ApiClient(String baseUrl, boolean followRedirect, Converter.Factory converter) {
        this(baseUrl, followRedirect, converter, BODY);
    }

    public ApiClient(String baseUrl, boolean followRedirect, Converter.Factory converter, Level loggingLevel) {
        this(baseUrl, followRedirect, converter, loggingLevel, null);
    }

    public ApiClient(String baseUrl,
                     boolean followRedirect,
                     Converter.Factory converter,
                     Level loggingLevel,
                     Interceptor... interceptors) {

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                okHttpClientBuilder.addNetworkInterceptor(interceptor);
            }
        }

        okHttpClient = okHttpClientBuilder
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(loggingLevel))
                .followRedirects(followRedirect).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .client(okHttpClient)
                .build();
    }

}
