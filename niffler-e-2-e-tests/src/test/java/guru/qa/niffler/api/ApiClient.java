package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.*;
import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

public abstract class ApiClient {
    protected static final Config CFG = Config.getInstance();

    protected final OkHttpClient okHttpClient;
    protected final Retrofit retrofit;

    public ApiClient(String buseUrl) {
        this(buseUrl, false, JacksonConverterFactory.create(), BODY);
    }

    public ApiClient(String buseUrl, Level logicLevel) {
        this(buseUrl, false, JacksonConverterFactory.create(), logicLevel);
    }

    public ApiClient(String buseUrl, boolean followRedirect) {
        this(buseUrl, followRedirect, JacksonConverterFactory.create(), BODY);
    }

    public ApiClient(String buseUrl, Converter.Factory converter) {
        this(buseUrl, false, converter, BODY);
    }

    public ApiClient(String buseUrl, boolean followRedirect, Converter.Factory converter) {
        this(buseUrl, followRedirect, converter, BODY);
    }

    public ApiClient(String baseUrl,
                     boolean followRedirect,
                     Converter.Factory converter,
                     Level loginLevel,
                     Interceptor... interceptors) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                okHttpClientBuilder.addNetworkInterceptor(interceptor);
            }
        }

        this.okHttpClient = okHttpClientBuilder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(loginLevel))
                .followRedirects(followRedirect)
                .build();

        this.retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .build();
    }
}
