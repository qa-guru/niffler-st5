package guru.qa.niffler.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Утилитный класс для генерации кода верификации и проверочного кода для PKCE (Proof Key for Code Exchange).
 */
public class OauthUtils {

    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * Генерирует случайный код верификации длиной 32 байта.
     *
     * @return сгенерированный код верификации в формате URL-безопасного Base64 без разделителей
     */
    public static String generateCodeVerifier() {
        byte[] codeVerifier = new byte[32];
        secureRandom.nextBytes(codeVerifier);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier);
    }

    /**
     * Вычисляет проверочный код из заданного кода верификации с использованием SHA-256.
     *
     * @param codeVerifier код верификации
     * @return вычисленный проверочный код в формате URL-безопасного Base64 без разделителей
     * @throws UnsupportedEncodingException если кодировка ASCII не поддерживается
     * @throws NoSuchAlgorithmException     если алгоритм SHA-256 не поддерживается
     */
    public static String generateCodeChallenge(String codeVerifier) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] digest = messageDigest.digest();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }
}
