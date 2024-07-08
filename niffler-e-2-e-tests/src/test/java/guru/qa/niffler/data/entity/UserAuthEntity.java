package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.UserJson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.FetchType.EAGER;

// Класс сущности для аутентификации пользователя
@Getter
@Setter
@Entity //добавить в resources/META-INF/persistence.xml
@Table(name = "\"user\"")
public class UserAuthEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id; // Уникальный идентификатор пользователя

    @Column(nullable = false, unique = true)
    private String username; // Имя пользователя

    @Column(nullable = false)
    private String password; // Пароль пользователя

    @Column(nullable = false)
    private Boolean enabled; // Флаг, указывающий, что учетная запись пользователя активна

    @Column(name = "account_non_expired", nullable = false)
    private Boolean accountNonExpired; // Флаг, указывающий, что срок действия учетной записи не истек

    @Column(name = "account_non_locked", nullable = false)
    private Boolean accountNonLocked; // Флаг, указывающий, что учетная запись пользователя не заблокирована

    @Column(name = "credentials_non_expired", nullable = false)
    private Boolean credentialsNonExpired; // Флаг, указывающий, что срок действия учетных данных пользователя не истек

    @OneToMany(fetch = EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
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

    public void addAuthorities(AuthorityEntity... authorities) {
        for (AuthorityEntity authority : authorities) {
            this.authorities.add(authority);
            authority.setUser(this);
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserEntity that = (UserEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
