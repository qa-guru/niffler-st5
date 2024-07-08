package guru.qa.niffler.api.cookie;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

import static java.net.CookiePolicy.ACCEPT_ALL;

/**
 * Реализация потокобезопасного хранилища cookie на основе ThreadLocal.
 * Используется для управления cookie в многопоточных средах.
 */
public enum ThreadSafeCookieStore implements CookieStore {
    INSTANCE;

    private final ThreadLocal<CookieStore> cookieManager = ThreadLocal.withInitial(
            () -> new CookieManager(null, ACCEPT_ALL).getCookieStore()
    );


    /**
     * Добавляет cookie в хранилище.
     *
     * @param uri    URI, связанный с cookie
     * @param cookie cookie для добавления
     */
    @Override
    public void add(URI uri, HttpCookie cookie) {
        cookieManager.get().add(uri, cookie);
    }

    /**
     * Получает cookie из хранилища по-заданному URI.
     *
     * @param uri URI, связанный с cookie
     * @return список cookie, соответствующих URI
     */
    @Override
    public List<HttpCookie> get(URI uri) {
        return cookieManager.get().get(uri);
    }

    /**
     * Получает все cookie из хранилища.
     *
     * @return список всех cookie в хранилище
     */
    @Override
    public List<HttpCookie> getCookies() {
        return cookieManager.get().getCookies();
    }

    /**
     * Получает список URI, связанных с cookie в хранилище.
     *
     * @return список URI, связанных с cookie
     */
    @Override
    public List<URI> getURIs() {
        return cookieManager.get().getURIs();
    }

    /**
     * Удаляет cookie из хранилища по заданному URI и значению cookie.
     *
     * @param uri    URI, связанный с cookie
     * @param cookie cookie для удаления
     * @return true, если cookie была успешно удалена
     */
    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return cookieManager.get().remove(uri, cookie);
    }

    /**
     * Удаляет все cookie из хранилища.
     *
     * @return true, если все cookie были успешно удалены
     */
    @Override
    public boolean removeAll() {
        return cookieManager.get().removeAll();
    }

    /**
     * Получает значение cookie по ее имени.
     *
     * @param cookieName имя cookie
     * @return значение cookie
     * @throws RuntimeException если cookie с заданным именем не найдена
     */
    public String getCookieValue(String cookieName) {
        return getCookies().stream()
                .filter(c -> c.getName().equals(cookieName))
                .findFirst()
                .map(HttpCookie::getValue)
                .orElseThrow();
    }

    /**
     * Очищает все cookie из хранилища.
     */
    public void clearCookies() {
        cookieManager.get().removeAll();
    }
}
