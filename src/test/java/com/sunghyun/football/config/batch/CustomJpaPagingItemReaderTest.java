package com.sunghyun.football.config.batch;

import com.sunghyun.football.domain.noti.infrastructure.entity.FreeSubNotiEntity;
import com.sunghyun.football.domain.noti.infrastructure.entity.FreeSubNotiHistoryEntity;
import jakarta.persistence.OneToMany;
import org.assertj.core.util.Lists;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


class CustomJpaPagingItemReaderTest {

    @Test
    void test() throws ClassNotFoundException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        TestBuilder<FreeSubNotiEntity> builder = new TestBuilder<>(
                FreeSubNotiEntity.builder().freeSubNotiHistories(Lists.newArrayList(new FreeSubNotiHistoryEntity())).build()
        );
        builder.test();
    }

    static class TestBuilder<T>{
        T entity;
        int price;

        public TestBuilder(T entity){this.entity=entity;}

        public T getEntity() {
            return entity;
        }

        public void test() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            T entity = this.getEntity();

            //클래스 이름
            String entityName = entity.getClass().toString().substring(6);
            
            //클래스 객체
            Class entityClass = Class.forName(entityName);

            //@OneToMany를 달고 있는 프로퍼티명을 찾아라.
            for(Field field:entity.getClass().getDeclaredFields()){
                //@OneToMany 어노테이션
                Annotation annotation = field.getAnnotation(OneToMany.class);

                //@OneToMany 어노테이션 존재하지 않으면 continue
                if(annotation==null) continue;

                //존재하면 해당 필드 초기화
                //해당 필드
                String fieldName = field.getName();

                //첫 글자
                String firstCharacter = fieldName.substring(0,1).toUpperCase();

                //메소드명 만들기
                String methodName = "get"+firstCharacter+fieldName.substring(1);

                //메소드 추출
                Method getFreeSubNotiHistoriesMethod= entityClass.getMethod("getFreeSubNotiHistories");

                //메소드 실행
                Object obj = getFreeSubNotiHistoriesMethod.invoke(entity);
            }
        }
    }

}