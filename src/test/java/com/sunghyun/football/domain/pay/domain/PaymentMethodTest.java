package com.sunghyun.football.domain.pay.domain;

import com.sunghyun.football.domain.pay.domain.model.PaymentMethod;
import com.sunghyun.football.global.exception.pay.exception.UnavailablePaymentMethodException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentMethodTest {

    @Test
    @DisplayName("flg가 1인 경우 에러 응답")
    void fail(){
        //given
        PaymentMethod paymentMethod= PaymentMethod.CREDIT_CARD;

        //when,then
        Assertions.assertThatThrownBy(paymentMethod::checkAvailable)
                        .isInstanceOf(UnavailablePaymentMethodException.class)
                ;
    }

    @Test
    @DisplayName("flg가 0인 경우 해당 예외 응답하지 않음")
    void success(){
        //given
        PaymentMethod paymentMethod= PaymentMethod.KAKAO_PAY;

        //when,then
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(paymentMethod::checkAvailable);
    }

}