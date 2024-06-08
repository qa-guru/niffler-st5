package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

// Интерфейс UserRepository определяет методы для работы с пользователями в базе данных
public interface UserRepository {

    // Статический метод getInstance() возвращает экземпляр UserRepository
    // в зависимости от значения системного свойства "repo"
    static UserRepository getInstance() {

        //todo-удалить после завершения работы с ДЗ
        //для дебага с разными репо
        System.setProperty("repo", "sjdbс");

        // Если свойство "repo" равно "sjdbс", возвращается экземпляр UserRepositorySpringJdbc
        if ("sjdbс".equals(System.getProperty("repo"))) {
            System.out.println("SPRING_JDBС");
            return new UserRepositorySpringJdbc();
        }
        // Если свойство "repo" равно "hibernate", возвращается экземпляр UserRepositoryHibernate
        if ("hibernate".equals(System.getProperty("repo"))) {
            System.out.println("HIBERNATE");
            return new UserRepositoryHibernate();
        }
        // Если ни одно из предыдущих условий не выполнено, возвращается экземпляр UserRepositoryJdbc
        System.out.println("JDBC");
        return new UserRepositoryJdbc();
    }

    // Метод createUserInAuth создает нового пользователя в таблице аутентификации
    // и принимает в качестве аргумента объект UserAuthEntity
    UserAuthEntity createUserInAuth(UserAuthEntity user);

    // Метод createUserInUserData создает нового пользователя в таблице данных пользователя
    // и принимает в качестве аргумента объект UserEntity
    UserEntity createUserInUserData(UserEntity user);

    // Метод findUserInUserDataById ищет пользователя в таблице данных пользователя по его идентификатору
    Optional<UserEntity> findUserInUserDataById(UUID id);

    // Метод updateUserInAuth обновляет информацию о пользователе в таблице аутентификации
    UserAuthEntity updateUserInAuth(UserAuthEntity user);

    // Метод updateUserInUserdata обновляет информацию о пользователе в таблице данных пользователя
    UserEntity updateUserInUserdata(UserEntity user);
}