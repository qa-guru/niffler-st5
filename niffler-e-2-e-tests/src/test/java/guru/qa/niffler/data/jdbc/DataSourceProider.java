package guru.qa.niffler.data.jdbc;

import guru.qa.niffler.data.DataBase;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum DataSourceProider {
    INSTANCE;

    private final Map<DataBase, DataSource> store = new ConcurrentHashMap<>();

    private DataSource computeDataSource(DataBase db) {
        return store.computeIfAbsent(db , key -> {
            PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
            pgSimpleDataSource.setURL(db.getJdbcUrl());
            pgSimpleDataSource.setUser("postgres");
            pgSimpleDataSource.setPassword("secret");
            return pgSimpleDataSource;
        });
    }

    public static DataSource dataSource(DataBase db) {
        return DataSourceProider.INSTANCE.computeDataSource(db);
    }
}
