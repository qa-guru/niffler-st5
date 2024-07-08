package guru.qa.niffler.api.interceptor;

import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Перехватчик для обработки ответов с кодом авторизации в потоке OAuth 2.0.
 */
public class CodeInterceptor implements Interceptor {

    /**
     * Перехватывает ответ цепочки запросов и обрабатывает код авторизации, если он присутствует.
     *
     * @param chain цепочка запросов
     * @return ответ с обработанным кодом авторизации
     * @throws IOException если возникает ошибка ввода-вывода при выполнении запроса
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (response.isRedirect() && response.header("Location").contains("code=")) {
            ApiLoginExtension.setCode(
                    StringUtils.substringAfter(
                            response.header("Location"),
                            "code="
                    )
            );
        }
        return response;
    }
}
