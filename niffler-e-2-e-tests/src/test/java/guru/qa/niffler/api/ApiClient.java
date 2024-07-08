package guru.qa.niffler.api;

import guru.qa.niffler.api.cookie.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import okhttp3.Interceptor; // Это интерфейс, который позволяет перехватывать и модифицировать HTTP-запросы и ответы. Он используется для добавления дополнительной логики, такой как авторизация, логирование и т.д.
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient; // Это основной клиент для выполнения HTTP-запросов. Он настраивается с помощью OkHttpClient. Builder и используется Retrofit для отправки запросов.
import okhttp3.logging.HttpLoggingInterceptor; // Это реализация Interceptor, которая позволяет логировать HTTP-трафик. Она используется для отладки и мониторинга API-взаимодействий.
import okhttp3.logging.HttpLoggingInterceptor.Level; // Это перечисление, которое определяет уровень детализации логирования HTTP-трафика (NONE, BASIC, HEADERS, BODY).
import retrofit2.Converter; // Это интерфейс, который определяет способ преобразования данных между Java-объектами и HTTP-сообщениями. В данном случае используется JacksonConverterFactory, который использует библиотеку Jackson для сериализации и десериализации JSON-данных.
import retrofit2.Retrofit; //  Это основной класс, который предоставляет высокоуровневый API для взаимодействия с удаленными API-интерфейсами. Он настраивается с помощью Retrofit.Builder и использует OkHttpClient для выполнения HTTP-запросов.
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.net.CookieManager;

import static java.net.CookiePolicy.ACCEPT_ALL;
import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

/**
 * Абстрактный базовый класс для клиентов API, предоставляющий общую конфигурацию и настройки.
 */
public abstract class ApiClient {

    // Синглтон-класс, который предоставляет доступ к конфигурационным данным
    // для получения необходимых настроек.
    protected static final Config CFG = Config.getInstance();

    protected final OkHttpClient okHttpClient;
    protected final Retrofit retrofit;

    /**
     * Конструктор для создания клиента API с базовым URL и настройками по умолчанию.
     *
     * @param baseUrl базовый URL API
     */
    public ApiClient(String baseUrl) {
        this(baseUrl, false, JacksonConverterFactory.create(), BODY);
    }

    /**
     * Конструктор для создания клиента API с базовым URL, уровнем логирования и настройками по умолчанию.
     *
     * @param baseUrl      базовый URL API
     * @param loggingLevel уровень логирования HTTP-трафика
     */
    public ApiClient(String baseUrl, Level loggingLevel) {
        this(baseUrl, false, JacksonConverterFactory.create(), loggingLevel);
    }

    /**
     * Конструктор для создания клиента API с базовым URL, конвертером данных и настройками по умолчанию.
     *
     * @param baseUrl   базовый URL API
     * @param converter фабрика конвертера данных
     */
    public ApiClient(String baseUrl, Converter.Factory converter) {
        this(baseUrl, false, converter, BODY);
    }

    /**
     * Конструктор для создания клиента API с базовым URL, конвертером данных, уровнем логирования и настройками по умолчанию.
     *
     * @param baseUrl      базовый URL API
     * @param converter    фабрика конвертера данных
     * @param loggingLevel уровень логирования HTTP-трафика
     */
    public ApiClient(String baseUrl, Converter.Factory converter, Level loggingLevel) {
        this(baseUrl, false, converter, loggingLevel);
    }

    /**
     * Конструктор для создания клиента API с базовым URL, настройкой перенаправления и настройками по умолчанию.
     *
     * @param baseUrl        базовый URL API
     * @param followRedirect флаг, указывающий, следует ли следовать за перенаправлениями
     */
    public ApiClient(String baseUrl, boolean followRedirect) {
        this(baseUrl, followRedirect, JacksonConverterFactory.create(), BODY);
    }

    /**
     * Конструктор для создания клиента API с базовым URL, настройкой перенаправления, уровнем логирования и настройками по умолчанию.
     *
     * @param baseUrl        базовый URL API
     * @param followRedirect флаг, указывающий, следует ли следовать за перенаправлениями
     * @param loggingLevel   уровень логирования HTTP-трафика
     */
    public ApiClient(String baseUrl, boolean followRedirect, Level loggingLevel) {
        this(baseUrl, followRedirect, JacksonConverterFactory.create(), loggingLevel);
    }

    /**
     * Конструктор для создания клиента API с базовым URL, настройкой перенаправления, конвертером данных и настройками по умолчанию.
     *
     * @param baseUrl        базовый URL API
     * @param followRedirect флаг, указывающий, следует ли следовать за перенаправлениями
     * @param converter      фабрика конвертера данных
     */
    public ApiClient(String baseUrl, boolean followRedirect, Converter.Factory converter) {
        this(baseUrl, followRedirect, converter, BODY);
    }

    /**
     * Конструктор для создания клиента API с базовым URL, настройкой перенаправления, конвертером данных, уровнем логирования и настройками по умолчанию.
     *
     * @param baseUrl        базовый URL API
     * @param followRedirect флаг, указывающий, следует ли следовать за перенаправлениями
     * @param converter      фабрика конвертера данных
     * @param loggingLevel   уровень логирования HTTP-трафика
     */
    public ApiClient(String baseUrl, boolean followRedirect, Converter.Factory converter, Level loggingLevel) {
        this(baseUrl, followRedirect, converter, loggingLevel, null);
    }

    /**
     * Основной конструктор для создания клиента API с полной настройкой.
     *
     * @param baseUrl        базовый URL API
     * @param followRedirect флаг, указывающий, следует ли следовать за перенаправлениями
     * @param converter      фабрика конвертера данных
     * @param loggingLevel   уровень логирования HTTP-трафика
     * @param interceptors   массив перехватчиков для добавления дополнительной логики
     */
    public ApiClient(String baseUrl,
                     boolean followRedirect,
                     Converter.Factory converter,
                     Level loggingLevel,
                     Interceptor... interceptors) {
        // Создаем билдер для OkHttpClient
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        // Если передан массив перехватчиков, добавляем их в билдер
        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                okHttpClientBuilder.addNetworkInterceptor(interceptor);
            }
        }

        // Настраиваем обработку cookie
        okHttpClientBuilder.cookieJar(
                new JavaNetCookieJar(
                        new CookieManager(ThreadSafeCookieStore.INSTANCE, ACCEPT_ALL)
                )
        );

        // Создаем OkHttpClient с настроенным билдером.
        // Добавляем логгер HTTP-трафика с указанным уровнем логирования.
        // Настраиваем следование перенаправлениям.
        this.okHttpClient = okHttpClientBuilder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(loggingLevel))
                .followRedirects(followRedirect)
                .build();

        // Создаем Retrofit с базовым URL, конвертером данных и настроенным OkHttpClient
        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .client(this.okHttpClient)
                .build();
    }
}
