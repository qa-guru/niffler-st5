package guru.qa.niffler.data.jpa;

import jakarta.persistence.*;
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

    private EntityManager emTL() {
        if (managerThreadLocal.get() == null) {
            managerThreadLocal.set(emf.createEntityManager());
        }
        return managerThreadLocal.get();
    }

    @Override
    public void close() {
        if (managerThreadLocal.get() == null) {
            managerThreadLocal.get().close();
            managerThreadLocal.set(null);
        }
    }

    @Override
    public void persist(Object o) {
        emTL().persist(o);
    }

    @Override
    public <T> T merge(T t) {
        return emTL().merge(t);
    }

    @Override
    public void remove(Object o) {
        emTL().remove(o);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o) {
        return emTL().find(aClass, o);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, Map<String, Object> map) {
        return emTL().find(aClass, o, map);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType) {
        return emTL().find(aClass, o, lockModeType);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType, Map<String, Object> map) {
        return emTL().find(aClass, o, lockModeType, map);
    }

    @Override
    public <T> T getReference(Class<T> aClass, Object o) {
        return emTL().getReference(aClass, o);
    }

    @Override
    public void flush() {
        emTL().flush();
    }

    @Override
    public void setFlushMode(FlushModeType flushModeType) {
        emTL().setFlushMode(flushModeType);
    }

    @Override
    public FlushModeType getFlushMode() {
        return emTL().getFlushMode();
    }

    @Override
    public void lock(Object o, LockModeType lockModeType) {
        emTL().lock(o, lockModeType);
    }

    @Override
    public void lock(Object o, LockModeType lockModeType, Map<String, Object> map) {
        emTL().lock(o, lockModeType, map);
    }

    @Override
    public void refresh(Object o) {
        emTL().refresh(o);
    }

    @Override
    public void refresh(Object o, Map<String, Object> map) {
        emTL().refresh(o, map);
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType) {
        emTL().refresh(o, lockModeType);
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType, Map<String, Object> map) {
        emTL().refresh(o, lockModeType, map);
    }

    @Override
    public void clear() {
        emTL().clear();
    }

    @Override
    public void detach(Object o) {
        emTL().detach(o);
    }

    @Override
    public boolean contains(Object o) {
        return emTL().contains(o);
    }

    @Override
    public LockModeType getLockMode(Object o) {
        return emTL().getLockMode(o);
    }

    @Override
    public void setProperty(String s, Object o) {
        emTL().setProperty(s, o);
    }

    @Override
    public Map<String, Object> getProperties() {
        return emTL().getProperties();
    }

    @Override
    public Query createQuery(String s) {
        return emTL().createQuery(s);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return emTL().createQuery(criteriaQuery);
    }

    @Override
    public Query createQuery(CriteriaUpdate criteriaUpdate) {
        return emTL().createQuery(criteriaUpdate);
    }

    @Override
    public Query createQuery(CriteriaDelete criteriaDelete) {
        return emTL().createQuery(criteriaDelete);
    }

    @Override
    public <T> TypedQuery<T> createQuery(String s, Class<T> aClass) {
        return emTL().createQuery(s, aClass);
    }

    @Override
    public Query createNamedQuery(String s) {
        return emTL().createNamedQuery(s);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String s, Class<T> aClass) {
        return emTL().createNamedQuery(s, aClass);
    }

    @Override
    public Query createNativeQuery(String s) {
        return emTL().createNativeQuery(s);
    }

    @Override
    public Query createNativeQuery(String s, Class aClass) {
        return emTL().createNativeQuery(s, aClass);
    }

    @Override
    public Query createNativeQuery(String s, String s1) {
        return emTL().createNativeQuery(s, s1);
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String s) {
        return emTL().createNamedStoredProcedureQuery(s);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s) {
        return emTL().createStoredProcedureQuery(s);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, Class... classes) {
        return emTL().createStoredProcedureQuery(s, classes);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, String... strings) {
        return emTL().createStoredProcedureQuery(s, strings);
    }

    @Override
    public void joinTransaction() {
        emTL().joinTransaction();
    }

    @Override
    public boolean isJoinedToTransaction() {
        return emTL().isJoinedToTransaction();
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return emTL().unwrap(aClass);
    }

    @Override
    public Object getDelegate() {
        return emTL().getDelegate();
    }

    @Override
    public boolean isOpen() {
        return emTL().isOpen();
    }

    @Override
    public EntityTransaction getTransaction() {
        return emTL().getTransaction();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return emTL().getEntityManagerFactory();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return emTL().getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return emTL().getMetamodel();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> aClass) {
        return emTL().createEntityGraph(aClass);
    }

    @Override
    public EntityGraph<?> createEntityGraph(String s) {
        return emTL().createEntityGraph(s);
    }

    @Override
    public EntityGraph<?> getEntityGraph(String s) {
        return emTL().getEntityGraph(s);
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> aClass) {
        return emTL().getEntityGraphs(aClass);
    }
}
