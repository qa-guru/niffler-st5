package guru.qa.niffler.model.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Класс для представления JSON-ответа, содержащего токены авторизации и другую информацию.
 *
 * @param accessToken  токен доступа
 * @param refreshToken токен обновления
 * @param scope        область действия токена
 * @param idToken      идентификационный токен
 * @param tokenType    тип токена
 * @param expiresIn    время жизни токена в секундах
 */
public record TokenJson(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("scope") String scope,
        @JsonProperty("id_token") String idToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") String expiresIn) {
}
