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
            //реализация датасорса для Постгрес
            HashMap<Object, Object> props = new HashMap<>();

            props.put("hibernate.connection.url", db.getJdbcUrl());
            props.put("hibernate.connection.username", "postgres");
            props.put("hibernate.connection.password", "secret");
            props.put("hibernate.connection.driver_class", "org.postgresql.Driver");
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
