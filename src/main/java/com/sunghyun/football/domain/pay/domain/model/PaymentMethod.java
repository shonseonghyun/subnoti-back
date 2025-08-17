package com.sunghyun.football.domain.pay.domain.model;

import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.pay.UnavailablePaymentMethodException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public enum PaymentMethod {
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
            throw new UnavailablePaymentMethodException(ErrorCode.UNAVAILABLE_PAYMENT_METHOD);
        }

        log.info("[{}] 사용 가능한 결제수단입니다.",this.desc);
    }
}
