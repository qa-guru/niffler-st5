package guru.qa.niffler.jpa;

import guru.qa.niffler.data.DataBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EmProvider {
    INSTANCE;

    private final Map<DataBase, EntityManagerFactory> store = new ConcurrentHashMap<>();

    private EntityManagerFactory computeEmf(DataBase db) {
        return store.computeIfAbsent(db, key -> {
            Map<String, String> props = new HashMap<>();

            props.put("hibernate.connection.url", db.getJdbsUrl());
            props.put("hibernate.connection.username", "postgres");
            props.put("hibernate.connection.password", "secret");
            props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            props.put("hibernate.connection.driver_class", "org.postgresql.Driver");
//props.put("hibernate.hbm2ddl.auto", "create");

            return Persistence.createEntityManagerFactory("niffler-st5",
                    props);
        });
    }

    public static EntityManager entityManager(DataBase db) {
        return new TransactionalEntityManager(
                new ThreadSafeEntityManager(
                        EmProvider.INSTANCE.computeEmf(db).createEntityManager()
                )
        );
    }
}
