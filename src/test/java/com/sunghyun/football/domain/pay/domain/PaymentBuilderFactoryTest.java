package com.sunghyun.football.domain.pay.domain;

import com.sunghyun.football.domain.pay.domain.model.PaymentMethod;
import com.sunghyun.football.domain.pay.domain.service.PaymentBuilderFactory;
import com.sunghyun.football.domain.pay.domain.service.PaymentProcessor;
import com.sunghyun.football.domain.pay.infrastructure.payment.processor.CreditCardPaymentProcessor;
import com.sunghyun.football.domain.pay.infrastructure.payment.processor.KakaoPaymentProcessor;
import com.sunghyun.football.domain.pay.infrastructure.payment.processor.NaverPaymentProcessor;
import com.sunghyun.football.global.exception.pay.exception.UnsupportedPaymentMethodException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentBuilderFactoryTest {

    private PaymentBuilderFactory paymentBuilderFactory;

    @BeforeEach
    void setUp(){
        List<PaymentProcessor> processorList = List.of(
                new KakaoPaymentProcessor(),
                new NaverPaymentProcessor(),
//                new CashPaymentProcessor(),
                new CreditCardPaymentProcessor()
        );

        paymentBuilderFactory = new PaymentBuilderFactory(processorList);
    }

    @Test
    @DisplayName("KAKAO_PAY 결제 수단에 맞는 구현체가 정상적으로 반환된다")
    void getKakaoProcessor() {
        PaymentProcessor processor = paymentBuilderFactory.getPaymentProcessor(PaymentMethod.KAKAO_PAY);
        assertThat(processor).isInstanceOf(KakaoPaymentProcessor.class);
    }

    @Test
    @DisplayName("미등록 결제수단 요청 시 UnsupportedPaymentMethodException 발생")
    void unknownPaymentMethodThrowsException() {
        assertThatThrownBy(() ->
                paymentBuilderFactory.getPaymentProcessor(PaymentMethod.CASH) // 이건 등록 안 했으니 에러 나야 함
        ).isInstanceOf(UnsupportedPaymentMethodException.class);
    }


}