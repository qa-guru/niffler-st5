package guru.qa.niffler.data.jpa;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class TransactionalEntityManager implements EntityManager {

    private static final Logger logger = LoggerFactory.getLogger(TransactionalEntityManager.class);

    // Объект, который будет использоваться для выполнения транзакций
    private final EntityManager delegate;

    public TransactionalEntityManager(EntityManager delegate) {
        this.delegate = delegate;
    }

    // Обертка для выполнения транзакции
    // принимает объект EntityManager и не возвращает результата.
    private void tx(Consumer<EntityManager> consumer) {
        // Начинаем транзакцию
        EntityTransaction entityTransaction = delegate.getTransaction();
        entityTransaction.begin();
        try {
            // Выполняем операцию
            consumer.accept(delegate);
            // Коммитим транзакцию
            entityTransaction.commit();
        } catch (Exception e) {
            // Роллбэк транзакции в случае ошибки
            entityTransaction.rollback();
            throw e;
        }
    }

    // Обертка для выполнения транзакции с возвращением результата,
    // принимает объект EntityManager и возвращает объект типа T.
    private <T> T txWithResult(Function<EntityManager, T> consumer) {
        // Начинаем транзакцию
        EntityTransaction entityTransaction = delegate.getTransaction();
        entityTransaction.begin();
        try {
            // Выполняем операцию
            T result = consumer.apply(delegate);
            // Коммитим транзакцию
            entityTransaction.commit();
            return result;
        } catch (Exception e) {
            // Роллбэк транзакции в случае ошибки
            entityTransaction.rollback();
            throw e;
        }
    }

    @Override
    // Persist - сохраняет объект в базе данных
    public void persist(Object entity) {
        tx(entityManager -> entityManager.persist(entity));
    }

    @Override
    // Merge - обновляет объект в базе данных
    public <T> T merge(T entity) {
        return txWithResult(entityManager -> entityManager.merge(entity));
    }

    @Override
    // Remove - удаляет объект из базы данных
    public void remove(Object entity) {
        tx(entityManager -> entityManager.remove(entity));
        System.out.println(entity.toString() + " DELETED");
    }

    @Override
    public <T> T find(Class<T> aClass, Object o) {
        return delegate.find(aClass, o);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, Map<String, Object> map) {
        return delegate.find(aClass, o, map);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType) {
        return delegate.find(aClass, o, lockModeType);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType, Map<String, Object> map) {
        return delegate.find(aClass, o, lockModeType, map);
    }

    @Override
    public <T> T getReference(Class<T> aClass, Object o) {
        return delegate.getReference(aClass, o);
    }

    @Override
    public void flush() {
        delegate.flush();
    }

    @Override
    public void setFlushMode(FlushModeType flushModeType) {
        delegate.setFlushMode(flushModeType);
    }

    @Override
    public FlushModeType getFlushMode() {
        return delegate.getFlushMode();
    }

    @Override
    public void lock(Object o, LockModeType lockModeType) {
        delegate.lock(o, lockModeType);
    }

    @Override
    public void lock(Object o, LockModeType lockModeType, Map<String, Object> map) {
        delegate.lock(o, lockModeType, map);
    }

    @Override
    public void refresh(Object o) {
        delegate.refresh(o);
    }

    @Override
    public void refresh(Object o, Map<String, Object> map) {
        delegate.refresh(o, map);
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType) {
        delegate.refresh(o, lockModeType);
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType, Map<String, Object> map) {
        delegate.refresh(o, lockModeType, map);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public void detach(Object o) {
        delegate.detach(o);
    }

    @Override
    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    @Override
    public LockModeType getLockMode(Object o) {
        return delegate.getLockMode(o);
    }

    @Override
    public void setProperty(String s, Object o) {
        delegate.setProperty(s, o);
    }

    @Override
    public Map<String, Object> getProperties() {
        return delegate.getProperties();
    }

    @Override
    public Query createQuery(String s) {
        return delegate.createQuery(s);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return delegate.createQuery(criteriaQuery);
    }

    @Override
    public Query createQuery(CriteriaUpdate criteriaUpdate) {
        return delegate.createQuery(criteriaUpdate);
    }

    @Override
    public Query createQuery(CriteriaDelete criteriaDelete) {
        return delegate.createQuery(criteriaDelete);
    }

    @Override
    public <T> TypedQuery<T> createQuery(String s, Class<T> aClass) {
        return delegate.createQuery(s, aClass);
    }

    @Override
    public Query createNamedQuery(String s) {
        return delegate.createNamedQuery(s);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String s, Class<T> aClass) {
        return delegate.createNamedQuery(s, aClass);
    }

    @Override
    public Query createNativeQuery(String s) {
        return delegate.createNativeQuery(s);
    }

    @Override
    public Query createNativeQuery(String s, Class aClass) {
        return delegate.createNativeQuery(s, aClass);
    }

    @Override
    public Query createNativeQuery(String s, String s1) {
        return delegate.createNativeQuery(s, s1);
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String s) {
        return delegate.createNamedStoredProcedureQuery(s);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s) {
        return delegate.createStoredProcedureQuery(s);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, Class... classes) {
        return delegate.createStoredProcedureQuery(s, classes);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, String... strings) {
        return delegate.createStoredProcedureQuery(s, strings);
    }

    @Override
    public void joinTransaction() {
        delegate.joinTransaction();
    }

    @Override
    public boolean isJoinedToTransaction() {
        return delegate.isJoinedToTransaction();
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return delegate.unwrap(aClass);
    }

    @Override
    public Object getDelegate() {
        return delegate.getDelegate();
    }

    @Override
    public void close() {
        delegate.close();
    }

    @Override
    public boolean isOpen() {
        return delegate.isOpen();
    }

    @Override
    public EntityTransaction getTransaction() {
        return delegate.getTransaction();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return delegate.getEntityManagerFactory();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return delegate.getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return delegate.getMetamodel();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> aClass) {
        return delegate.createEntityGraph(aClass);
    }

    @Override
    public EntityGraph<?> createEntityGraph(String s) {
        return delegate.createEntityGraph(s);
    }

    @Override
    public EntityGraph<?> getEntityGraph(String s) {
        return delegate.getEntityGraph(s);
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> aClass) {
        return delegate.getEntityGraphs(aClass);
    }
}
