package com.sunghyun.football.config.batch.reader;

import com.sunghyun.football.global.utils.EntityDtoConverter;
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
public class DtoConvertJpaPagingItemReader<E,D> extends AbstractPagingItemReader<D> {

    private EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    private final Map<String, Object> jpaPropertyMap = new HashMap<>();

    private String queryString;

    private JpaQueryProvider queryProvider;

    private Map<String, Object> parameterValues;

    private boolean transacted = true;// default value

    public Class<D> dtoClass;

    public DtoConvertJpaPagingItemReader() {
        setName(ClassUtils.getShortName(JpaPagingItemReader.class));
    }

    private Query createQuery() {
        if (queryProvider == null) {
            return entityManager.createQuery(queryString);
        }
        else {
            return queryProvider.createQuery();
        }
    }

    public void setDtoClass(Class<D> dtoClass) {
        this.dtoClass=dtoClass;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void setParameterValues(Map<String, Object> parameterValues) {
        this.parameterValues = parameterValues;
    }


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

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

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

        EntityTransaction tx = null;

        if (transacted) {
            tx = entityManager.getTransaction();
            tx.begin();

            entityManager.flush();
            entityManager.clear();
        } // end if

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

        List<E> queryResult = query.getResultList();
        List<D> queryResultDto = EntityDtoConverter.convertListFromEntityToDto(queryResult,dtoClass);

        results.addAll(queryResultDto);

        /* 또 다른 문제 발생 */
        //만약 1개가 변환실패로 4개만 변환하여 results에 addALl한 경우 다음 페이지 읽을지 말지 조건 중 읽지 말고 그대로 종료되는 조건에 걸림
        //조건: 페이지 사이보다 작게 읽은 경우(4개만 읽어서 더 이상 읽을게 없다고 판단)

        if (!transacted) {
            for (E entity : queryResult) {
                entityManager.detach(entity);
            } // end if
        }
        else {
            tx.commit();
        } // end if
    }

    @Override
    protected void doClose() throws Exception {
        entityManager.close();
        super.doClose();
    }


}
