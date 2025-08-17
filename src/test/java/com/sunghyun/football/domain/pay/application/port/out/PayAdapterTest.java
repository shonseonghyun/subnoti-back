package com.sunghyun.football.domain.pay.application.port.out;

import com.sunghyun.football.domain.pay.domain.model.Pay;
import com.sunghyun.football.domain.pay.domain.model.PaymentMethod;
import com.sunghyun.football.domain.pay.domain.repository.PayRepository;
import com.sunghyun.football.domain.pay.domain.service.PaymentBuilderFactory;
import com.sunghyun.football.domain.pay.domain.service.PaymentProcessor;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.pay.PayNotFoundException;
import com.sunghyun.football.global.exception.exceptions.pay.UnsupportedPaymentMethodException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayAdapterTest {

    private final Long memberNo = 1L;
    private final int rightAmount = 9900;
    private final Long subscriptionNo= 1L;
    private final PaymentMethod activePaymentMethod = PaymentMethod.KAKAO_PAY;


    @InjectMocks
    private PayAdapter target;

    @Mock
    private PaymentBuilderFactory paymentBuilderFactory;

    @Mock
    private PayRepository payRepository;

    @Mock
    private PaymentProcessor paymentProcessor;


    @Test
    void 결제_환불_시_SubscriptionNo로_조회_시_pay도메인_없는_경우_PayNotFoundException_응답(){
        //given
        when(payRepository.findBySubscriptionNo(subscriptionNo))
                .thenReturn(Optional.empty())
        ;

        assertThatThrownBy(()->target.refund(subscriptionNo))
                .isInstanceOf(PayNotFoundException.class)
        ;
    }

    @Test
    void 결제_환불_시_존재하지_않는_결제수단이면_UnsupportedPaymentMethodException_응답(){
        //given
        Pay pay = Pay.create(memberNo,subscriptionNo,rightAmount,activePaymentMethod);
        when(payRepository.findBySubscriptionNo(subscriptionNo))
                .thenReturn(Optional.ofNullable(pay))
        ;
        when(paymentBuilderFactory.getPaymentProcessor(any()))
                .thenThrow(new UnsupportedPaymentMethodException(ErrorCode.UNSUPPORTED_PAYMENT_METHOD))
        ;

        assertThatThrownBy(()->target.refund(subscriptionNo))
                .isInstanceOf(UnsupportedPaymentMethodException.class)
        ;
    }

    @Test
    void 결제_환불_성공_시_결제저장이_호출된다(){
        //given
        Pay pay = Pay.create(memberNo,subscriptionNo,rightAmount,activePaymentMethod);

        when(payRepository.findBySubscriptionNo(subscriptionNo))
                .thenReturn(Optional.ofNullable(pay))
        ;
        when(paymentBuilderFactory.getPaymentProcessor(any()))
                .thenReturn(paymentProcessor);
        when(payRepository.save(any(Pay.class)))
                .thenReturn(pay)
        ;

        //when
        target.refund(subscriptionNo);

        //then
        verify(payRepository,times(1))
                .save(pay);
    }

}