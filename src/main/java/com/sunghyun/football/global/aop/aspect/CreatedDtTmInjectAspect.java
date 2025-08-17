package com.sunghyun.football.global.aop.aspect;

import com.sunghyun.football.global.aop.Auditable;
import com.sunghyun.football.global.utils.MatchDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class CreatedDtTmInjectAspect {
    @AfterReturning(
            pointcut = "execution(* com.sunghyun.football..create*(..))",
            returning = "result"
    )
    public void injectCreatedDtTm(JoinPoint joinPoint, Object result) {
        // 프록시 경유 여부 확인용 로그
        boolean isProxy = org.springframework.aop.support.AopUtils.isAopProxy(joinPoint.getTarget());
        boolean isJdkProxy = org.springframework.aop.support.AopUtils.isJdkDynamicProxy(joinPoint.getTarget());
        boolean isCglibProxy = org.springframework.aop.support.AopUtils.isCglibProxy(joinPoint.getTarget());

        log.info("[AOP] CreatedDtTmInjectAspect 실행됨 → 메소드: {}.{}(..), 반환타입: {}, 프록시: {}, JDKProxy: {}, CGLIBProxy: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                result != null ? result.getClass().getSimpleName() : "null",
                isProxy, isJdkProxy, isCglibProxy);

        if(result instanceof Auditable auditable){
            String prevDt= auditable.getCreatedDt();
            String prevTm = auditable.getCreatedTm();

            if(prevDt == null || prevTm == null){
                String nowDt = MatchDateUtils.getNowDtStr();
                String nowTm = MatchDateUtils.getNowTmStr();

                auditable.injectCreatedDtAndTm(nowDt,nowTm);

                log.info("[AOP] 생성일시 주입 완료 → {} (createdDt: {}, createdTm: {})",
                        result.getClass().getSimpleName(), nowDt, nowTm);
            }
        }
        else{
            log.warn("[AOP] Auditable 대상 아님 → 실제 타입: {}",
                    result != null ? result.getClass().getName() : "null");
        }
    }
}
