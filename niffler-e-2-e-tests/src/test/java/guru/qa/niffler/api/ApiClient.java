package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import okhttp3.Interceptor; // Это интерфейс, который позволяет перехватывать и модифицировать HTTP-запросы и ответы. Он используется для добавления дополнительной логики, такой как авторизация, логирование и т.д.
import okhttp3.OkHttpClient; // Это основной клиент для выполнения HTTP-запросов. Он настраивается с помощью OkHttpClient. Builder и используется Retrofit для отправки запросов.
import okhttp3.logging.HttpLoggingInterceptor; // Это реализация Interceptor, которая позволяет логировать HTTP-трафик. Она используется для отладки и мониторинга API-взаимодействий.
import okhttp3.logging.HttpLoggingInterceptor.Level; // Это перечисление, которое определяет уровень детализации логирования HTTP-трафика (NONE, BASIC, HEADERS, BODY).
import retrofit2.Converter; // Это интерфейс, который определяет способ преобразования данных между Java-объектами и HTTP-сообщениями. В данном случае используется JacksonConverterFactory, который использует библиотеку Jackson для сериализации и десериализации JSON-данных.
import retrofit2.Retrofit; //  Это основной класс, который предоставляет высокоуровневый API для взаимодействия с удаленными API-интерфейсами. Он настраивается с помощью Retrofit.Builder и использует OkHttpClient для выполнения HTTP-запросов.
import retrofit2.converter.jackson.JacksonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

public abstract class ApiClient {

    // Синглтон-класс, который предоставляет доступ к конфигурационным данным
    // для получения необходимых настроек.
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

    public ApiClient(String baseUrl,
                     boolean followRedirect,
                     Converter.Factory convertor,
                     Level loggingLevel,
                     Interceptor... interceptors) {

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                okHttpClientBuilder.addNetworkInterceptor(interceptor);
            }
        }

        this.okHttpClient =
                okHttpClientBuilder
                        .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(loggingLevel))
                        .followRedirects(followRedirect)
                        .build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(convertor)
                .client(this.okHttpClient)
                .build();
    }
}
