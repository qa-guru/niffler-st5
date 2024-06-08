package guru.qa.niffler.data.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

// Класс сущности для полномочий
@Getter
@Setter
public class AuthorityEntity implements Serializable {
    // Уникальный идентификатор полномочия
    private UUID id;

    // Тип полномочия (чтение или запись)
    private Authority authority;

    // Уникальный идентификатор пользователя, которому принадлежит это полномочие
    private UUID user_id;
}
