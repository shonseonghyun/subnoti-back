package com.sunghyun.football.domain.pay.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PayTest {
    private final Long memberNo = 1L;
    private final int rightAmount = 9900;
    private final Long subscriptionNo= 1L;
    private final PaymentMethod activePaymentMethod = PaymentMethod.KAKAO_PAY;
    private final PaymentMethod inactivePaymentMethod = PaymentMethod.CREDIT_CARD;
    
    @Test
    void 결제_환불_상태_변경(){
        //given
        Pay pay= Pay.create(memberNo,subscriptionNo,rightAmount,activePaymentMethod);

        //when
        pay.refund();

        //then
        assertThat(pay.getPayStatus()).isEqualTo(PayStatus.REFUND);
    }

    @Test
    void 결제_성공_상태_변경(){
        //given
        Pay pay= Pay.create(memberNo,subscriptionNo,rightAmount,activePaymentMethod);

        //when
        pay.pay();

        //then
        assertThat(pay.getPayStatus()).isEqualTo(PayStatus.SUCCESS);
    }
}