package com.sunghyun.football.config.batch;

import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class CustomJpaPagingItemReader<E,D> extends AbstractPagingItemReader<D> {

    private EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    private final Map<String, Object> jpaPropertyMap = new HashMap<>();

    private String queryString;

    private JpaQueryProvider queryProvider;

    private Map<String, Object> parameterValues;

    private boolean transacted = true;// default value

    public Class<D> dtoClass;

    public CustomJpaPagingItemReader() {
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

//    private Query createAssociationQuery() {
//        if (queryProvider == null) {
//            return entityManager.createQuery(associationQueryString);
//        }
//        else {
//            return queryProvider.createQuery();
//        }
//    }

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
        List<D> queryResultDto = new ArrayList<D>(queryResult.size());

        for(E entity:queryResult){
            try{
                //DTO 인스턴스 생성
                D dto = dtoClass.getDeclaredConstructor().newInstance();

                //클래스 이름
                String entityName = entity.getClass().toString().substring(6);

                //클래스 객체
                Class<?> entityClass = Class.forName(entityName);

                // 조회 대상 엔티티의 @OneToMany를 달고 있는 프로퍼티 찾아서
                //  1.연관관계 프록시 강제 초기화 수행
                // 2.DTO로 변환
                for(Field field:entity.getClass().getDeclaredFields()){
                    //@OneToMany 어노테이션
                    Annotation annotation = field.getAnnotation(OneToMany.class);

                    //@OneToMany 어노테이션 존재하지 않으면 continue
                    if(annotation==null) continue;

                    //존재하면 아래 실행
                    //해당 프로퍼티명
                    String fieldName = field.getName();
                    field.setAccessible(true);

                    //프로퍼티 첫 문자를 대문자 치환
                    String firstCharacter = fieldName.substring(0,1).toUpperCase();

                    //메소드명 만들기 ex) getFreeSubNotiHistoryEntity
                    String methodName = "get"+firstCharacter+fieldName.substring(1);

                    //메소드 추출
                    Method getAssociationEntityMethod= entityClass.getMethod(methodName);
                    List<Object> associationEntityList = (List<Object>) getAssociationEntityMethod.invoke(entity);

                    //프록시 강제 초기화
                    Hibernate.initialize(associationEntityList);

                    //DTO에 @OneToMany 달린 필드명과 동일한 필드 찾기
                    Field dtoField = dtoClass.getDeclaredField(fieldName);
                    dtoField.setAccessible(true);
                    ParameterizedType pt = (ParameterizedType) dtoField.getGenericType();
                    Class<?> childDtoType = (Class<?>) pt.getActualTypeArguments()[0];

                    List<Object> childDtos = new ArrayList<>(associationEntityList.size());
                    for(Object childEntity: associationEntityList){
                        Object childDto = childDtoType.getDeclaredConstructor().newInstance();
                        BeanUtils.copyProperties(childEntity,childDto);
                        childDtos.add(childDto);
                    }

                    dtoField.set(dto,childDtos);
                }

                //Entity와 Dto 사이에서 값을 복사(컬렉션 제외)
                BeanUtils.copyProperties(entity,dto);

                // add
                queryResultDto.add(dto);
            }catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException | InstantiationException | NoSuchFieldException e){
                log.error("에러 발생");
                e.printStackTrace();
            }
        }


        //if/else문의 result add 하는 부분을 하나로 통합
        results.addAll(queryResultDto);

        if (!transacted) {
            for (E entity : queryResult) {
                entityManager.detach(entity);
//                results.add(entity);
            } // end if
        }
        else {
//            results.addAll(queryResult);
            tx.commit();
        } // end if
    }

    @Override
    protected void doClose() throws Exception {
        entityManager.close();
        super.doClose();
    }


}
