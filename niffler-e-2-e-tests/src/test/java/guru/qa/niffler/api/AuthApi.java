package guru.qa.niffler.api;

import guru.qa.niffler.model.oauth.TokenJson;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Интерфейс для определения API-методов аутентификации с использованием OAuth 2.0.
 */
public interface AuthApi {

    // Исходное состояние:
    // codeChallenge, codeVerifier


    // ПЕРВЫЙ ЗАПРОС
    // GET http://127.0.0.1:9000/oauth2/authorize
    // ?response_type=code
    // &client_id=client
    // &scope=openid
    // &redirect_uri=http://127.0.0.1:3000/authorized
    // &code_challenge=TUNQUxOID-FcWw7fc5QHlsKBPFCc-octgzcHu9KMplo
    // &code_challenge_method=S256

    /**
     * Метод для выполнения предварительного запроса на авторизацию.
     *
     * @param responseType        тип ответа (должен быть "code")
     * @param clientId            идентификатор клиента
     * @param scope               область действия токена
     * @param redirectUri         URI перенаправления после авторизации
     * @param codeChallenge       проверочный код для метода PKCE
     * @param codeChallengeMethod метод проверочного кода для PKCE (должен быть "S256")
     * @return вызов Retrofit для выполнения запроса
     */
    @GET("oauth2/authorize")
    Call<Void> preRequest(
            @Query("response_type") String responseType,
            @Query("client_id") String clientId,
            @Query("scope") String scope,
            @Query(value = "redirect_uri", encoded = true) String redirectUri,
            @Query("code_challenge") String codeChallenge,
            @Query("code_challenge_method") String codeChallengeMethod
    );

   /*
    PKCE (Proof Key for Code Exchange) - это расширение потока авторизационного кода OAuth 2.0, предназначенное для предотвращения атак CSRF и инъекции кода авторизации.
    PKCE работает следующим образом:
    Клиент генерирует случайный code_verifier и вычисляет из него code_challenge.
    Клиент отправляет code_challenge серверу авторизации при запросе кода авторизации.
    Сервер авторизации сохраняет code_challenge и возвращает код авторизации клиенту.
    Клиент отправляет код авторизации и code_verifier серверу авторизации для обмена на токены.
    Сервер авторизации проверяет code_verifier против сохраненного code_challenge. Если они совпадают, сервер возвращает токены.
   */

    // Response
    // 302 Found -> http://127.0.0.1:9000/login
    // Set-Cookie: JSESSIONID=742917C05962B24D2FA40A3611EE5E7D
    // Auto Redirect -> GET http://127.0.0.1:9000/login
    // 200 OK
    // Set-Cookie: XSRF-TOKEN=1427f5de-d014-467f-9fc5-3a8cce1cfed6

    /**
     * Метод для выполнения запроса на вход в систему.
     *
     * @param csrf     токен CSRF
     * @param username имя пользователя
     * @param password пароль
     * @return вызов Retrofit для выполнения запроса
     */
    @POST("login")
    @FormUrlEncoded
    Call<Void> login(
            @Field("_csrf") String csrf,
            @Field("username") String username,
            @Field("password") String password
    );

    // ВТОРОЙ ЗАПРОС
    // POST http://127.0.0.1:9000/login
    // Cookies: XSRF-TOKEN=1427f5de-d014-467f-9fc5-3a8cce1cfed6, JSESSIONID=742917C05962B24D2FA40A3611EE5E7D
    // URL ENCODED FIELD: _csrf: 1427f5de-d014-467f-9fc5-3a8cce1cfed6
    // URL ENCODED FIELD: username: dima
    // URL ENCODED FIELD: password: 12345

    // Response
    // 302 Found -> http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=http://127.0.0.1:3000/authorized&code_challenge=TUNQUxOID-FcWw7fc5QHlsKBPFCc-octgzcHu9KMplo&code_challenge_method=S256&continue
    // Set-Cookie: JSESSIONID=73C4CFC638B6BF650461F95704EE26BB; Path=/
    // Set-Cookie: XSRF-TOKEN=; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:10 GMT; Path=/
    // Auto Redirect -> GET http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=http://127.0.0.1:3000/authorized&code_challenge=TUNQUxOID-FcWw7fc5QHlsKBPFCc-octgzcHu9KMplo&code_challenge_method=S256&continue
    // Response
    // 302 Found -> http://127.0.0.1:3000/authorized?code=u8AbPxCWftqGEjpRhjk9ceOqI9IIInedEd-ac6eTgTYOyQheNHHmKyGDuf3uryafM5d2lQmtLbJBuxWw-pA-PH8SwKTe-cQbp93b3CB2NZucerT1nFmQMNGw-AvIcSsP
    // Auto Redirect -> GET http://127.0.0.1:3000/authorized?code=u8AbPxCWftqGEjpRhjk9ceOqI9IIInedEd-ac6eTgTYOyQheNHHmKyGDuf3uryafM5d2lQmtLbJBuxWw-pA-PH8SwKTe-cQbp93b3CB2NZucerT1nFmQMNGw-AvIcSsP
    // 200 OK

    /**
     * Метод для получения токенов доступа и обновления.
     *
     * @param basicAuthorizationHeader заголовок авторизации в формате "Basic base64(client:secret)"
     * @param code                     код авторизации, полученный в ответе на предыдущий запрос
     * @param redirectUri              URI перенаправления после авторизации
     * @param codeVerifier             верификационный код для метода PKCE
     * @param grantType                тип гранта (должен быть "authorization_code")
     * @param clientId                 идентификатор клиента
     * @return вызов Retrofit для выполнения запроса, возвращающий объект TokenJson с токенами
     */
    @POST("oauth2/token")
    @FormUrlEncoded
    Call<TokenJson> token(
            @Header("Authorization") String basicAuthorizationHeader,
            @Field("code") String code,
            @Field(value = "redirect_uri", encoded = true) String redirectUri,
            @Field("code_verifier") String codeVerifier,
            @Field("grant_type") String grantType,
            @Field("client_id") String clientId
    );

    // ТРЕТИЙ ЗАПРОС
    // POST -> http://127.0.0.1:9000/oauth2/token
    // URL ENCODED FIELD: code: u8AbPxCWftqGEjpRhjk9ceOqI9IIInedEd-ac6eTgTYOyQheNHHmKyGDuf3uryafM5d2lQmtLbJBuxWw-pA-PH8SwKTe-cQbp93b3CB2NZucerT1nFmQMNGw-AvIcSsP
    // URL ENCODED FIELD: redirect_uri: http://127.0.0.1:3000/authorized
    // URL ENCODED FIELD: code_verifier: sT2C2AF_Y_F_JrlfOItvCZOhm7sIF_ltCTF9_VLucB4
    // URL ENCODED FIELD: grant_type: authorization_code
    // URL ENCODED FIELD: client_id: client
    // Header: Authorization: Basic Y2xpZW50OnNlY3JldA==
    // Response
    // 200 OK
//    {
//            "access_token": "eyJraWQiOiIwM2Q5M2Q5OC1iMDBiLTQ3NWYtYWVhZC05ZmZmNDM3NGYwMjMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJkaW1hIiwiYXVkIjoiY2xpZW50IiwibmJmIjoxNzE4MjE0MDQ2LCJzY29wZSI6WyJvcGVuaWQiXSwiaXNzIjoiaHR0cDovLzEyNy4wLjAuMTo5MDAwIiwiZXhwIjoxNzE4MjE3NjQ2LCJpYXQiOjE3MTgyMTQwNDYsImp0aSI6IjcyNjA4YmZhLTYzZTgtNDBlNC05ZTQ3LTk3ZTIyMjVjMTRkMiJ9.jLI4ZW4G6mUBXU5KPcs32venaSQNyD8PHlIfSJfhDmRVysTv0TRkOa47_-DbF86Lu0SW_y3j-0y-HwALGAEPRkyPbtmHRKWY8FNpOY2a8H7F5GHOtm1b3vEZBqbbLoPNM1louo5yJLZSdaXyHE7uvgXKhpeKOQW9fmZtDmtNDOAZuTPeo8x8ZFCNpFc_6fBXxo6Q8bpQYIjOPJ9fX4Yutm7MBn9FDLJqTTmB4ZVdJnjcgWt5dCw7mY8H6mXBhfJtCQJrCSsCBkXRj7ahbw8uffzLZhUHqvEHK3BQg3MPDI7xPhVC8hPeNWuVYQdc4X9lHxHoZrr5wFqBIaXZ7yPPAQ",
//            "refresh_token": "wN_zI2EV_XCuECCyvukKKgjrXXsCH3_Sgyep6jfBAJ6TcZNNwfmcfnCPIHbjKY0GaBcPA3cl2aHWlnUR9tXv9qoFW8MlUulS2XUD9iu-2S0zYVYyv1U8120g0LhPwnKp",
//            "scope": "openid",
//            "id_token": "eyJraWQiOiIwM2Q5M2Q5OC1iMDBiLTQ3NWYtYWVhZC05ZmZmNDM3NGYwMjMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJkaW1hIiwiYXVkIjoiY2xpZW50IiwiYXpwIjoiY2xpZW50IiwiYXV0aF90aW1lIjoxNzE4MjE0MDQ2LCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjkwMDAiLCJleHAiOjE3MTgyMTU4NDYsImlhdCI6MTcxODIxNDA0NiwianRpIjoiYWMyZGVkMDItMzY4Ny00NmMzLWIxZWItYzA3ZmNjNGU0NDlmIiwic2lkIjoiU3NVT21VT2J6bkRTbnF2QU5WbmkxeFowNTBpeUdKc29tY0xHdlN4eE8xNCJ9.LolKOw4Zz-zIiSwPaF9aPdWpobEYURIORnZqvHQb2YUOB44Np8UfGq6iMntsmscaAOtdY6Kq3hlicXG010xlDcMOgDFTmDIy0LZye0yEnTsBnefwqGkohTpEk-8yVPJxxpawNAKDworWssPLX0rRqqqXY1gPh3OaRwbrlUMr3E6Prf7pe6LRVJO7i5T_Giw2EQVOhILQRkEwXS7WG3IAFJKXoJF039uPwd_hlsD_t2kpOCppykvSYYonCJjJlRxCA-D1AM55glzpJe6Ir4g4ZWAv0qCvpTfw3TGW-cLFL3zAoiz2S3P8XDVZsOduvg2M_bbA4aPnb_kePR9MjnPiZw",
//            "token_type": "Bearer",
//            "expires_in": 3599
//    }


}
