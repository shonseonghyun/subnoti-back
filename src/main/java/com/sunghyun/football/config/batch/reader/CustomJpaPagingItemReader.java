package com.sunghyun.football.config.batch.reader;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class CustomJpaPagingItemReader<T> extends AbstractPagingItemReader<T> {

    private EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    private EntityManager em;

    private final Map<String, Object> jpaPropertyMap = new HashMap<>();

    private String queryString;

    private JpaQueryProvider queryProvider;

    private Map<String, Object> parameterValues;

    private boolean transacted = true;// default value

    public CustomJpaPagingItemReader() {
        setName(ClassUtils.getShortName(JpaPagingItemReader.class));
    }

    /**
     * Create a query using an appropriate query provider (entityManager OR
     * queryProvider).
     */
    private Query createQuery() {
        if (queryProvider == null) {
            return entityManager.createQuery(queryString);
        }
        else {
            return queryProvider.createQuery();
        }
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * The parameter values to be used for the query execution.
     * @param parameterValues the values keyed by the parameter named used in the query
     * string.
     */
    public void setParameterValues(Map<String, Object> parameterValues) {
        this.parameterValues = parameterValues;
    }

    /**
     * By default (true) the EntityTransaction will be started and committed around the
     * read. Can be overridden (false) in cases where the JPA implementation doesn't
     * support a particular transaction. (e.g. Hibernate with a JTA transaction). NOTE:
     * may cause problems in guaranteeing the object consistency in the
     * EntityManagerFactory.
     * @param transacted indicator
     */
    public void setTransacted(boolean transacted) {
        this.transacted = transacted;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        if (queryProvider == null) {
            Assert.state(entityManagerFactory != null, "EntityManager is required when queryProvider is null");
            Assert.state(StringUtils.hasLength(queryString), "Query string is required when queryProvider is null");
        }
    }

    /**
     * @param queryString JPQL query string
     */
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    /**
     * @param queryProvider JPA query provider
     */
    public void setQueryProvider(JpaQueryProvider queryProvider) {
        this.queryProvider = queryProvider;
    }

    @Override
    protected void doOpen() throws Exception {
        super.doOpen();

        entityManager = entityManagerFactory.createEntityManager(jpaPropertyMap);
        if (entityManager == null) {
            throw new DataAccessResourceFailureException("Unable to obtain an EntityManager");
        }
        // set entityManager to queryProvider, so it participates
        // in JpaPagingItemReader's managed transaction
        if (queryProvider != null) {
            queryProvider.setEntityManager(entityManager);
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doReadPage() {
        if (transacted) {
            entityManager.clear();
        }//end if

        Query query = createQuery().setFirstResult(getPage() * getPageSize()).setMaxResults(getPageSize());

        if (parameterValues != null) {
            for (Map.Entry<String, Object> me : parameterValues.entrySet()) {
                query.setParameter(me.getKey(), me.getValue());
            }
        }

        if (results == null) {
            results = new CopyOnWriteArrayList<>();
        }
        else {
            results.clear();
        }

        if (!transacted) {
            List<T> queryResult = query.getResultList();
            for (T entity : queryResult) {
                entityManager.detach(entity);
                results.add(entity);
            }//end if
        } else {
            results.addAll(query.getResultList());
        }//end if
    }


    @Override
    protected void doClose() throws Exception {
        entityManager.close();
        super.doClose();
    }

    public void setEm(EntityManager em) {
        this.em=em;
    }

    public EntityManager getEm(){
        return em;
    }
}
