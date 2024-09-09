package com.sunghyun.football;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.RestDocumentationContext;
import org.springframework.restdocs.RestDocumentationContextProvider;

@TestConfiguration
public class TestConfig {

    @Bean
    public RestDocumentationContextProvider restDocumentationContextProvider(){
        return new RestDocumentationContextProvider() {
            @Override
            public RestDocumentationContext beforeOperation() {
                return null;
            }
        };
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(entityManager);
    }
}
