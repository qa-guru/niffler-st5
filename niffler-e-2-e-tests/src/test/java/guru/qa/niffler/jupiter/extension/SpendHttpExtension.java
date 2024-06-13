package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.extension.ExtensionContext;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Objects;

import static guru.qa.niffler.utils.DateHelper.convertStringToDate;
import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

public class SpendHttpExtension extends AbstractSpendExtension {

    // Создаем клиент для HTTP-запросов
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            // Добавляем интерцептор для логирования HTTP-запросов
            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(BODY))
            .build();

    // Создаем retrofit для создания API
    private final Retrofit retrofit = new Retrofit.Builder()
            // Устанавливаем клиент для HTTP-запросов
            .client(okHttpClient)
            // Устанавливаем базовую URL для API
            .baseUrl("http://127.0.0.1:8093/")
            // Добавляем конвертер для сериализации JSON
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    // Метод для создания объекта расхода
    @Override
    protected SpendJson createSpend(ExtensionContext extensionContext, Spend spend, CategoryJson category) {
        // Создаем экземпляр API для создания расходов
        SpendApi spendApi = retrofit.create(SpendApi.class);

        // Создаем объект расхода
        SpendJson spendJson = new SpendJson(
                null,
                // Конвертируем дату из строки в формат Date
                convertStringToDate(spend.spendDate()),
                spend.category(),
                spend.currency(),
                spend.amount(),
                spend.description(),
                category.username()
        );

        try {
            // Создаем расход с помощью API
            return Objects.requireNonNull(
                    spendApi.createSpend(spendJson).execute().body()
            );
        } catch (IOException e) {
            // В случае ошибки бросаем исключение
            throw new RuntimeException(e);
        }
    }

    // Метод для удаления объекта расхода
    @Override
    protected void removeSpend(SpendEntity spend) {
    }
}