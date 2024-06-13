package guru.qa.niffler.api.interceptor;

import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class CodeInterceptor implements Interceptor {
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
