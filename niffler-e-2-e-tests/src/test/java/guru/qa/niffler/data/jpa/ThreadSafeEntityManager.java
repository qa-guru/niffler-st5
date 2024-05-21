package guru.qa.niffler.data.jpa;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;

import java.util.List;
import java.util.Map;

public class ThreadSafeEntityManager implements EntityManager {

    private final EntityManagerFactory emf;
    private final ThreadLocal<EntityManager> managerThreadLocal = new ThreadLocal<>();

    public ThreadSafeEntityManager(EntityManager delegate) {
        this.emf = delegate.getEntityManagerFactory();
        this.managerThreadLocal.set(delegate);
    }

    private EntityManager emTl() {
        if (managerThreadLocal.get() == null) {
            managerThreadLocal.set(emf.createEntityManager());
        }
        return managerThreadLocal.get();
    }

    @Override
    public void close() {
        if (managerThreadLocal.get() != null) {
            managerThreadLocal.get().close();
            managerThreadLocal.set(null);
        }
    }

    @Override
    public void persist(Object entity) {
        emTl().persist(entity);
    }

    @Override
    public <T> T merge(T entity) {
        return emTl().merge(entity);
    }

    @Override
    public void remove(Object entity) {
        emTl().remove(entity);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey) {
        return emTl().find(entityClass, primaryKey);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
        return emTl().find(entityClass, primaryKey, properties);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
        return emTl().find(entityClass, primaryKey, lockMode);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
        return emTl().find(entityClass, primaryKey, lockMode, properties);
    }

    @Override
    public <T> T getReference(Class<T> entityClass, Object primaryKey) {
        return emTl().getReference(entityClass, primaryKey);
    }

    @Override
    public void flush() {
        emTl().flush();
    }

    @Override
    public void setFlushMode(FlushModeType flushMode) {
        emTl().setFlushMode(flushMode);
    }

    @Override
    public FlushModeType getFlushMode() {
        return emTl().getFlushMode();
    }

    @Override
    public void lock(Object entity, LockModeType lockMode) {
        emTl().lock(entity, lockMode);
    }

    @Override
    public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        emTl().lock(entity, lockMode, properties);
    }

    @Override
    public void refresh(Object entity) {
        emTl().refresh(entity);
    }

    @Override
    public void refresh(Object entity, Map<String, Object> properties) {
        emTl().refresh(entity, properties);
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode) {
        emTl().refresh(entity, lockMode);
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        emTl().refresh(entity, lockMode, properties);
    }

    @Override
    public void clear() {
        emTl().clear();
    }

    @Override
    public void detach(Object entity) {
        emTl().detach(entity);
    }

    @Override
    public boolean contains(Object entity) {
        return emTl().contains(entity);
    }

    @Override
    public LockModeType getLockMode(Object entity) {
        return emTl().getLockMode(entity);
    }

    @Override
    public void setProperty(String propertyName, Object value) {
        emTl().setProperty(propertyName, value);
    }

    @Override
    public Map<String, Object> getProperties() {
        return emTl().getProperties();
    }

    @Override
    public Query createQuery(String qlString) {
        return emTl().createQuery(qlString);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return emTl().createQuery(criteriaQuery);
    }

    @Override
    public Query createQuery(CriteriaUpdate updateQuery) {
        return emTl().createQuery(updateQuery);
    }

    @Override
    public Query createQuery(CriteriaDelete deleteQuery) {
        return emTl().createQuery(deleteQuery);
    }

    @Override
    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
        return emTl().createQuery(qlString, resultClass);
    }

    @Override
    public Query createNamedQuery(String name) {
        return emTl().createNamedQuery(name);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
        return emTl().createNamedQuery(name, resultClass);
    }

    @Override
    public Query createNativeQuery(String sqlString) {
        return emTl().createNativeQuery(sqlString);
    }

    @Override
    public Query createNativeQuery(String sqlString, Class resultClass) {
        return emTl().createNativeQuery(sqlString, resultClass);
    }

    @Override
    public Query createNativeQuery(String sqlString, String resultSetMapping) {
        return emTl().createNativeQuery(sqlString, resultSetMapping);
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
        return emTl().createNamedStoredProcedureQuery(name);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
        return emTl().createStoredProcedureQuery(procedureName);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
        return emTl().createStoredProcedureQuery(procedureName, resultClasses);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
        return emTl().createStoredProcedureQuery(procedureName, resultSetMappings);
    }

    @Override
    public void joinTransaction() {
        emTl().joinTransaction();
    }

    @Override
    public boolean isJoinedToTransaction() {
        return emTl().isJoinedToTransaction();
    }

    @Override
    public <T> T unwrap(Class<T> cls) {
        return emTl().unwrap(cls);
    }

    @Override
    public Object getDelegate() {
        return emTl().getDelegate();
    }

    @Override
    public boolean isOpen() {
        return emTl().isOpen();
    }

    @Override
    public EntityTransaction getTransaction() {
        return emTl().getTransaction();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return emTl().getEntityManagerFactory();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return emTl().getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return emTl().getMetamodel();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
        return emTl().createEntityGraph(rootType);
    }

    @Override
    public EntityGraph<?> createEntityGraph(String graphName) {
        return emTl().createEntityGraph(graphName);
    }

    @Override
    public EntityGraph<?> getEntityGraph(String graphName) {
        return emTl().getEntityGraph(graphName);
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
        return emTl().getEntityGraphs(entityClass);
    }
}
