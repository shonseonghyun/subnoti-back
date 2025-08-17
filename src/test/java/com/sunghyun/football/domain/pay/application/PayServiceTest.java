package com.sunghyun.football.domain.pay.application;

import com.sunghyun.football.domain.pay.application.port.out.SubscriptionServicePortForPay;
import com.sunghyun.football.domain.pay.domain.model.Pay;
import com.sunghyun.football.domain.pay.domain.model.PaymentMethod;
import com.sunghyun.football.domain.pay.domain.repository.PayRepository;
import com.sunghyun.football.domain.pay.domain.service.PayCreationService;
import com.sunghyun.football.domain.pay.domain.service.PaymentBuilderFactory;
import com.sunghyun.football.domain.pay.domain.service.PaymentProcessor;
import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.pay.exception.UnavailablePaymentMethodException;
import com.sunghyun.football.global.exception.pay.exception.UnsupportedPaymentMethodException;
import com.sunghyun.football.global.exception.subscription.exception.InvalidSubscriptionAmountException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayServiceTest {
    private final Long memberNo = 1L;
    private final int rightAmount = 9900;
    private final int wrongAmount=9;
    private final Long subscriptionNo= 1L;
    private final PaymentMethod activePaymentMethod = PaymentMethod.KAKAO_PAY;
    private final PaymentMethod inactivePaymentMethod = PaymentMethod.CREDIT_CARD;


    @InjectMocks
    private PayService target;

    @Mock
    private PayCreationService payCreationService;

    @Mock
    private PaymentBuilderFactory paymentBuilderFactory;

    @Mock
    private PayRepository payRepository;

    @Mock
    private PaymentProcessor paymentProcessor;

    @Mock
    private SubscriptionServicePortForPay subscriptionServicePort;

    @Test
    void 사용_불가한_결제수단이면_UnavailablePaymentMethodException_발생(){
        //given
        when(payCreationService.createPayDomain(any(),any(),anyInt(),any()))
                .thenThrow(new UnavailablePaymentMethodException(ErrorType.UNAVAILABLE_PAYMENT_METHOD));
        ;

        //when,then
        assertThatThrownBy(()->target.pay(memberNo,rightAmount,activePaymentMethod))
                .isInstanceOf(UnavailablePaymentMethodException.class);
    }

    @Test
    void 결제수단_없으면_UnsupportedPaymentMethodException_발생(){
        //given
        when(paymentBuilderFactory.getPaymentProcessor(any(PaymentMethod.class)))
                .thenThrow(new UnsupportedPaymentMethodException(ErrorType.UNSUPPORTED_PAYMENT_METHOD))
        ;

        //when,then
        assertThatThrownBy(()->target.pay(memberNo,rightAmount,inactivePaymentMethod))
                .isInstanceOf(UnsupportedPaymentMethodException.class);
    }

    @Test
    void 구독_생성_실패_시_InvalidSubscriptionAmountException_발생(){
        //given
        //검증 로직 순서가 바뀌면서 주석처리
//        Pay pay = Pay.create(memberNo,wrongAmount,activePaymentMethod);
//        when(payCreationService.createPay(anyLong(),anyInt(),any()))
//                .thenReturn(pay);
//        when(paymentBuilderFactory.getPaymentProcessor(any()))
//                .thenReturn(paymentProcessor);
//        when(payRepository.save(any(Pay.class)))
//                .thenReturn(pay)
//        ;
        doThrow(new InvalidSubscriptionAmountException(ErrorType.INVALID_AMOUNT))
                .when(subscriptionServicePort).createSubscription(anyLong(), anyInt(), anyString());

        assertThatThrownBy(()->target.pay(memberNo,wrongAmount,inactivePaymentMethod))
                .isInstanceOf(InvalidSubscriptionAmountException.class);

    }

    @Test
    void 정상_결제_시_결제저장과_구독생성이_호출된다(){
        //given
        Pay pay = Pay.create(memberNo,subscriptionNo,rightAmount,activePaymentMethod);
        when(payCreationService.createPayDomain(anyLong(),any(),anyInt(),any()))
                .thenReturn(pay);
        when(paymentBuilderFactory.getPaymentProcessor(any()))
                .thenReturn(paymentProcessor);
        when(payRepository.save(any(Pay.class)))
                .thenReturn(pay)
        ;
        //when
        target.pay(memberNo,rightAmount,activePaymentMethod);

        //then
        verify(payRepository,times(1)).save(any(Pay.class));
        verify(subscriptionServicePort,times(1)).createSubscription(anyLong(),anyInt(), anyString());
    }



//    @Test
//    void 결제_환불_시_담당객체를_통한_환불_실패_시_??_응답(){
//        //given
//        Pay pay = Pay.create(memberNo,subscriptionNo,rightAmount,activePaymentMethod);
//        when(payRepository.findBySubscriptionNo(subscriptionNo))
//                .thenReturn(Optional.ofNullable(pay))
//        ;
//        when(paymentBuilderFactory.getPaymentProcessor(any()))
//                .thenThrow(new UnsupportedPaymentMethodException(ErrorType.UNSUPPORTED_PAYMENT_METHOD))
//        ;
//
//        assertThatThrownBy(()->target.refund(subscriptionNo))
//                .isInstanceOf(UnsupportedPaymentMethodException.class)
//        ;
//    }


}