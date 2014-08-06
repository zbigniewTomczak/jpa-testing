package tomczak.rules.rules;

import org.junit.rules.ExternalResource;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;
import java.util.Map;

public class StandaloneEntityManagerFactory extends ExternalResource implements EntityManagerFactory {
    private final String unitName;
    private EntityManagerFactory entityManagerFactory;

    public StandaloneEntityManagerFactory(String unitName) {
        this.unitName = unitName;
    }

    @Override
    protected void before() {
        entityManagerFactory = Persistence.createEntityManagerFactory(unitName);
    }

    @Override
    protected void after() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    @Override
    public EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    @Override
    public EntityManager createEntityManager(Map map) {
        return entityManagerFactory.createEntityManager(map);
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType) {
        return entityManagerFactory.createEntityManager(synchronizationType);
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
        return entityManagerFactory.createEntityManager(synchronizationType, map);
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return entityManagerFactory.getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return entityManagerFactory.getMetamodel();
    }

    @Override
    public boolean isOpen() {
        return entityManagerFactory.isOpen();
    }

    @Override
    public void close() {
        entityManagerFactory.close();
    }

    @Override
    public Map<String, Object> getProperties() {
        return entityManagerFactory.getProperties();
    }

    @Override
    public Cache getCache() {
        return entityManagerFactory.getCache();
    }

    @Override
    public PersistenceUnitUtil getPersistenceUnitUtil() {
        return entityManagerFactory.getPersistenceUnitUtil();
    }

    @Override
    public void addNamedQuery(String s, Query query) {
        entityManagerFactory.addNamedQuery(s, query);
    }

    @Override
    public <T> T unwrap(Class<T> tClass) {
        return entityManagerFactory.unwrap(tClass);
    }

    @Override
    public <T> void addNamedEntityGraph(String s, EntityGraph<T> tEntityGraph) {
        entityManagerFactory.addNamedEntityGraph(s, tEntityGraph);
    }
}
