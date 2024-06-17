package guru.qa.niffler.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

// Класс сущности для полномочий
@Getter
@Setter
@Entity //добавить в resources/META-INF/persistence.xml
@Table(name = "authority")
public class AuthorityEntity implements Serializable {
    // Уникальный идентификатор полномочия
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    // Тип полномочия (чтение или запись)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Authority authority;

    // Уникальный идентификатор пользователя, которому принадлежит это полномочие
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAuthEntity user;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        AuthorityEntity that = (AuthorityEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
