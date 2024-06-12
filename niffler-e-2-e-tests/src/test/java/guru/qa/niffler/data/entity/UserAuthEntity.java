package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Класс сущности для аутентификации пользователя
@Getter
@Setter
public class UserAuthEntity implements Serializable {
    private UUID id; // Уникальный идентификатор пользователя
    private String username; // Имя пользователя
    private String password; // Пароль пользователя
    private Boolean enabled; // Флаг, указывающий, что учетная запись пользователя активна
    private Boolean accountNonExpired; // Флаг, указывающий, что срок действия учетной записи не истек
    private Boolean accountNonLocked; // Флаг, указывающий, что учетная запись пользователя не заблокирована
    private Boolean credentialsNonExpired; // Флаг, указывающий, что срок действия учетных данных пользователя не истек
    private List<AuthorityEntity> authorities = new ArrayList<>(); // Список полномочий пользователя

    // Метод для создания сущности UserAuthEntity из JSON-объекта пользователя
    public static UserAuthEntity fromJson(UserJson userJson) {
        // Создание сущностей полномочий для чтения и записи
        AuthorityEntity read = new AuthorityEntity();
        read.setAuthority(Authority.read);
        AuthorityEntity write = new AuthorityEntity();
        write.setAuthority(Authority.write);

        // Создание сущности UserAuthEntity
        UserAuthEntity userAuthEntity = new UserAuthEntity();

        userAuthEntity.setUsername(userJson.username());
        userAuthEntity.setPassword(userJson.testData().password());
        userAuthEntity.setEnabled(true);
        userAuthEntity.setAccountNonExpired(true);
        userAuthEntity.setAccountNonLocked(true);
        userAuthEntity.setCredentialsNonExpired(true);
        userAuthEntity.setAuthorities(List.of(read, write));

        return userAuthEntity;
    }
}
