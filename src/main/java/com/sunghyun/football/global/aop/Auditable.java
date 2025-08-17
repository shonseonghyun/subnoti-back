package com.sunghyun.football.global.aop;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Auditable
 *
 * <p>
 * 도메인 엔티티가 생성될 때 생성 일자(createdDt)와 생성 시간(createdTm)을 공통으로 관리하기 위한 추상 클래스입니다.
 * 이 클래스를 상속받은 도메인 객체는 생성 시점 정보를 자동으로 주입받을 수 있습니다.
 * </p>
 *
 * <p>
 * 생성 일자/시간 정보는 감사 로그, 비즈니스 로직에서의 시간 판단 등 다양한 목적으로 활용됩니다.
 * 생성 시점 정보는 외부에서 직접 설정하지 않고, {@link #injectCreatedDtAndTm(String, String)} 메서드를 통해
 * AOP 등의 자동 주입 방식으로 세팅됩니다.
 * </p>
 *
 * <h3>동작 방식</h3>
 * <ul>
 *   <li>도메인 객체 생성 후 서비스 계층에서 반환 시점에 AOP {@code @AfterReturning}이 동작</li>
 *   <li>반환된 객체가 {@code Auditable} 타입이면 현재 날짜/시간을 {@link #injectCreatedDtAndTm(String, String)} 로 주입</li>
 *   <li>생성 일자와 시간은 {@code String} 타입으로 관리되며, 읽기 전용 {@code @Getter} 제공</li>
 * </ul>
 *
 * <h3>사용 예시</h3>
 * <pre>
 * public class Subscription extends Auditable {
 *     // 구독권 도메인 클래스
 * }
 * </pre>
 *
 * <p>이후 {@code Subscription} 객체가 생성될 때 자동으로 생성 일시가 채워집니다.</p>
 *
 * @author YourName
 * @since 1.0
 */
@Setter
@Getter
@Slf4j
public abstract class Auditable {
    private String createdDt;
    private String createdTm;


    public void injectCreatedDtAndTm(String createdDt, String createdTm){
        this.createdDt = createdDt;
        this.createdTm=createdTm;
    }
}
