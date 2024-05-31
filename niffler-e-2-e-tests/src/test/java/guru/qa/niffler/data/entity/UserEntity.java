package guru.qa.niffler.data.entity;

import guru.qa.niffler.enums.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
	private UUID id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private CurrencyValues currency;

	@Column()
	private String firstname;

	@Column()
	private String surname;

	@Column(name = "photo", columnDefinition = "bytea")
	private byte[] photo;

	@Column(name = "photo_small", columnDefinition = "bytea")
	private byte[] photoSmall;

	@OneToMany(mappedBy = "requester", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FriendshipEntity> friendshipRequests = new ArrayList<>();

	@OneToMany(mappedBy = "addressee", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FriendshipEntity> friendshipAddressees = new ArrayList<>();

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

	static public UserEntity fromJson(UserJson userJson) {
		UserEntity user = new UserEntity();
		user.setUsername(userJson.username());
		user.setCurrency(userJson.currency());
		user.setFirstname(userJson.firstname());
		user.setSurname(userJson.surname());
		return user;
	}
}