package com.sunghyun.football.domain.pay.domain.model;

import com.sunghyun.football.domain.enumMapper.enums.EnumMapperType;
import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.pay.exception.UnavailablePaymentMethodException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public enum PaymentMethod implements EnumMapperType {
    KAKAO_PAY("카카오 페이",0),
    NAVER_PAY("네이버 페이",0),
    CASH("현금",0),
    CREDIT_CARD("카드",1)
    ;

    private final String desc;
    private final int flg;

    public void checkAvailable() {
        if(this.flg==1){
//            log.warn("[{}] 현재 사용 불가한 결제수단입니다.", this.desc); //도메인 서비스엔 로그를 남기지 않고 application 에서 catch로 잡아 남기자.
            throw new UnavailablePaymentMethodException(ErrorType.UNAVAILABLE_PAYMENT_METHOD);
        }

        log.info("[{}] 사용 가능한 결제수단입니다.",this.desc);
    }

    @Override
    public String getName() {
        return name();
    }
}
