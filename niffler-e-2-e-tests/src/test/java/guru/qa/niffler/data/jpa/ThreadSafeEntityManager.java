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
	private final ThreadLocal<EntityManager> manager0ThreadLocal = new ThreadLocal<>();

	public ThreadSafeEntityManager(EntityManager delegate) {
		this.emf = delegate.getEntityManagerFactory();
		this.manager0ThreadLocal.set(delegate);
	}

	private EntityManager emTk() {
		if (manager0ThreadLocal.get() == null) {
			manager0ThreadLocal.set(emf.createEntityManager());
		}
		return manager0ThreadLocal.get();
	}

	@Override
	public void close() {
		if (manager0ThreadLocal.get() != null) {
			manager0ThreadLocal.get().close();
			manager0ThreadLocal.set(null);
		}
	}

	@Override
	public void persist(Object entity) {
		emTk().persist(entity);
	}

	@Override
	public <T> T merge(T entity) {
		return emTk().merge(entity);
	}

	@Override
	public void remove(Object entity) {
		emTk().remove(entity);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey) {
		return emTk().find(entityClass, primaryKey);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
		return emTk().find(entityClass, primaryKey, properties);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
		return emTk().find(entityClass, primaryKey, lockMode);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
		return emTk().find(entityClass, primaryKey, lockMode, properties);
	}

	@Override
	public <T> T getReference(Class<T> entityClass, Object primaryKey) {
		return emTk().getReference(entityClass, primaryKey);
	}

	@Override
	public void flush() {
		emTk().flush();
	}

	@Override
	public void setFlushMode(FlushModeType flushMode) {
		emTk().setFlushMode(flushMode);
	}

	@Override
	public FlushModeType getFlushMode() {
		return emTk().getFlushMode();
	}

	@Override
	public void lock(Object entity, LockModeType lockMode) {
		emTk().lock(entity, lockMode);
	}

	@Override
	public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
		emTk().lock(entity, lockMode, properties);
	}

	@Override
	public void refresh(Object entity) {
		emTk().refresh(entity);
	}

	@Override
	public void refresh(Object entity, Map<String, Object> properties) {
		emTk().refresh(entity, properties);
	}

	@Override
	public void refresh(Object entity, LockModeType lockMode) {
		emTk().refresh(entity, lockMode);
	}

	@Override
	public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
		emTk().refresh(entity, lockMode, properties);
	}

	@Override
	public void clear() {
		emTk().clear();
	}

	@Override
	public void detach(Object entity) {
		emTk().detach(entity);
	}

	@Override
	public boolean contains(Object entity) {
		return emTk().contains(entity);
	}

	@Override
	public LockModeType getLockMode(Object entity) {
		return emTk().getLockMode(entity);
	}

	@Override
	public void setProperty(String propertyName, Object value) {
		emTk().setProperty(propertyName, value);
	}

	@Override
	public Map<String, Object> getProperties() {
		return emTk().getProperties();
	}

	@Override
	public Query createQuery(String qlString) {
		return emTk().createQuery(qlString);
	}

	@Override
	public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
		return emTk().createQuery(criteriaQuery);
	}

	@Override
	public Query createQuery(CriteriaUpdate updateQuery) {
		return emTk().createQuery(updateQuery);
	}

	@Override
	public Query createQuery(CriteriaDelete deleteQuery) {
		return emTk().createQuery(deleteQuery);
	}

	@Override
	public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
		return emTk().createQuery(qlString, resultClass);
	}

	@Override
	public Query createNamedQuery(String name) {
		return emTk().createNamedQuery(name);
	}

	@Override
	public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
		return emTk().createNamedQuery(name, resultClass);
	}

	@Override
	public Query createNativeQuery(String sqlString) {
		return emTk().createNativeQuery(sqlString);
	}

	@Override
	public Query createNativeQuery(String sqlString, Class resultClass) {
		return emTk().createNativeQuery(sqlString, resultClass);
	}

	@Override
	public Query createNativeQuery(String sqlString, String resultSetMapping) {
		return emTk().createNativeQuery(sqlString, resultSetMapping);
	}

	@Override
	public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
		return emTk().createNamedStoredProcedureQuery(name);
	}

	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
		return emTk().createStoredProcedureQuery(procedureName);
	}

	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
		return emTk().createStoredProcedureQuery(procedureName, resultClasses);
	}

	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
		return emTk().createStoredProcedureQuery(procedureName, resultSetMappings);
	}

	@Override
	public void joinTransaction() {
		emTk().joinTransaction();
	}

	@Override
	public boolean isJoinedToTransaction() {
		return emTk().isJoinedToTransaction();
	}

	@Override
	public <T> T unwrap(Class<T> cls) {
		return emTk().unwrap(cls);
	}

	@Override
	public Object getDelegate() {
		return emTk().getDelegate();
	}

	@Override
	public boolean isOpen() {
		return emTk().isOpen();
	}

	@Override
	public EntityTransaction getTransaction() {
		return emTk().getTransaction();
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return emTk().getEntityManagerFactory();
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return emTk().getCriteriaBuilder();
	}

	@Override
	public Metamodel getMetamodel() {
		return emTk().getMetamodel();
	}

	@Override
	public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
		return emTk().createEntityGraph(rootType);
	}

	@Override
	public EntityGraph<?> createEntityGraph(String graphName) {
		return emTk().createEntityGraph(graphName);
	}

	@Override
	public EntityGraph<?> getEntityGraph(String graphName) {
		return emTk().getEntityGraph(graphName);
	}

	@Override
	public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
		return emTk().getEntityGraphs(entityClass);
	}
}