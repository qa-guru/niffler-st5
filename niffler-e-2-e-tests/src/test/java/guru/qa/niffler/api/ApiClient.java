package guru.qa.niffler.api;

import guru.qa.niffler.api.cookie.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.net.CookieManager;

import static java.net.CookiePolicy.ACCEPT_ALL;
import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

public abstract class ApiClient {

    protected static final Config CFG = Config.getInstance();

    protected final OkHttpClient okHttpClient;
    protected final Retrofit retrofit;

    public ApiClient(String baseUrl) {
        this(baseUrl, false, JacksonConverterFactory.create(), BODY);
    }

    public ApiClient(String baseUrl, Level loggingLevel) {
        this(baseUrl, false, JacksonConverterFactory.create(), loggingLevel);
    }

    public ApiClient(String baseUrl, Converter.Factory converter) {
        this(baseUrl, false, converter, BODY);
    }

    public ApiClient(String baseUrl, Converter.Factory converter, Level loggingLevel) {
        this(baseUrl, false, converter, loggingLevel);
    }

    public ApiClient(String baseUrl, boolean followRedirect) {
        this(baseUrl, followRedirect, JacksonConverterFactory.create(), BODY);
    }

    public ApiClient(String baseUrl, boolean followRedirect, Level loggingLevel) {
        this(baseUrl, followRedirect, JacksonConverterFactory.create(), loggingLevel);
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

        okHttpClientBuilder.cookieJar(
                new JavaNetCookieJar(
                        new CookieManager(ThreadSafeCookieStore.INSTANCE, ACCEPT_ALL)
                )
        );

        this.okHttpClient = okHttpClientBuilder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(loggingLevel))
                .followRedirects(followRedirect)
                .build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .client(this.okHttpClient)
                .build();
    }
}
