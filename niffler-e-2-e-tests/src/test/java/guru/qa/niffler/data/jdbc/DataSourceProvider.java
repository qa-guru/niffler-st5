package guru.qa.niffler.data.jdbc;

import guru.qa.niffler.data.DataBase;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//enum - синглтон
public enum DataSourceProvider {
    INSTANCE;

    // хранилище dataSources
    private final Map<DataBase, DataSource> store = new ConcurrentHashMap<>();

    // вернёт датасорс по ключу, если есть, иначе добавит и вернёт
    private DataSource computeDataSource(DataBase db) {
        // если в store по ключу db ничего нет, то в лямда положим
        return store.computeIfAbsent(db, key -> {
            //реализация датасорса для Постгрес
            PGSimpleDataSource pgDataSource = new PGSimpleDataSource();
            pgDataSource.setURL(db.getJdbcUrl());
            pgDataSource.setUser("postgres");
            pgDataSource.setPassword("secret");
            return pgDataSource;
        });
    }

    public static DataSource dataSource(DataBase db) {
        return DataSourceProvider.INSTANCE.computeDataSource(db);
    }

}
