package guru.qa.niffler.data.jdbc;

import com.p6spy.engine.spy.P6DataSource;
import guru.qa.niffler.data.DataBase;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Отвечает за предоставление объектов DataSource для различных баз данных, определенных в перечислении DataBase.
 */
//enum - синглтон (в приложении будет существовать только один экземпляр этого класса)
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
            return new P6DataSource(pgDataSource);

            /* использование P6DataSource вместо обычного DataSource позволяет получить дополнительные возможности
             по мониторингу и отладке работы приложения с базами данных без необходимости вносить изменения в основной
              код приложения */
        });
    }

    /**
     * Метод-обертка вокруг computeDataSource().
     * Позволяет получить объект DataSource для указанной базы данных db без необходимости создавать экземпляр класса DataSourceProvider
     *
     * @param db
     * @return
     */
    public static DataSource dataSource(DataBase db) {
        return DataSourceProvider.INSTANCE.computeDataSource(db);
    }

}
