package guru.qa.niffler.data.jpa;

import guru.qa.niffler.data.DataBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//enum - синглтон
//Emf - entity manager factory
public enum EmProvider {
    INSTANCE;

    // хранилище dataSources
    private final Map<DataBase, EntityManagerFactory> store = new ConcurrentHashMap<>();

    // вернёт датасорс по ключу, если есть, иначе добавит и вернёт
    private EntityManagerFactory computeEmf(DataBase db) {
        // если в store по ключу db ничего нет, то в лямда положим
        return store.computeIfAbsent(db, key -> {
            // Реализация датасорса для Постгрес
            // Создание HashMap для хранения свойств конфигурации Hibernate.
            HashMap<Object, Object> props = new HashMap<>();

            // Установка URL-адреса базы данных, используя getP6spyUrl() из DataBase
            props.put("hibernate.connection.url", db.getP6spyUrl());
            // Установка имени пользователя и пароля для подключения к базе данных
            props.put("hibernate.connection.username", "postgres");
            props.put("hibernate.connection.password", "secret");

            /*Замена props.put("hibernate.connection.driver_class", "org.postgresql.Driver");
             на props.put("hibernate.connection.driver_class", "com.p6spy.engine.spy.P6SpyDriver");
              связана с использованием библиотеки P6Spy для мониторинга и логирования SQL-запросов.*/

            //props.put("hibernate.connection.driver_class", "org.postgresql.Driver");

            // Установка драйвера соединения с базой данных на com.p6spy.engine.spy.P6SpyDriver
            props.put("hibernate.connection.driver_class", "com.p6spy.engine.spy.P6SpyDriver");
            // Установка диалекта Hibernate для PostgreSQL
            props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

            // читаем resources/META-INF/persistence.xml
            return Persistence.createEntityManagerFactory(
                    "niffler-st5", props);
        });
    }

    // Возвращает EntityManager для указанной базы данных
    public static EntityManager entityManager(DataBase dataBase) {
        return new ThreadSafeEntityManager(
                new TransactionalEntityManager(
                        EmProvider.INSTANCE.computeEmf(dataBase).createEntityManager()
                )
        );
    }

}
