package guru.qa.niffler.data.jdbc;

import com.p6spy.engine.spy.P6DataSource;
import guru.qa.niffler.data.Database;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum DataSourceProvider {
    INSTANCE;

    private final Map<Database, DataSource> store = new ConcurrentHashMap<>();

    public static DataSource dataSource(Database database) {
        return DataSourceProvider.INSTANCE.computeDataSource(database);
    }

    private DataSource computeDataSource(Database database) {
        return store.computeIfAbsent(database, key -> {
            PGSimpleDataSource pgDataSource = new PGSimpleDataSource();
            pgDataSource.setURL(database.getJdbcUrl());
            pgDataSource.setUser("postgres");
            pgDataSource.setPassword("secret");
            return new P6DataSource(pgDataSource);
        });
    }

}