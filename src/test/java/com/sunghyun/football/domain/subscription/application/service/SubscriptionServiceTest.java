package com.sunghyun.football.domain.subscription.application.service;

import com.sunghyun.football.domain.subscription.application.service.port.out.PayServicePortForSubscription;
import com.sunghyun.football.domain.subscription.domain.model.Subscription;
import com.sunghyun.football.domain.subscription.domain.model.SubscriptionPlan;
import com.sunghyun.football.domain.subscription.domain.repository.SubscriptionRepository;
import com.sunghyun.football.domain.subscription.domain.service.SubscriptionDeletionService;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.pay.PayNotFoundException;
import com.sunghyun.football.global.exception.exceptions.subscription.SubscriptionNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @InjectMocks
    private SubscriptionService target;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private SubscriptionDeletionService subscriptionDeletionService;

    @Mock
    private PayServicePortForSubscription payServicePortForSubscription;

    @Test
    @DisplayName("cancel: 존재하지 않는 구독권이면 SubscriptionNotFoundException 발생")
    void cancel_존재하지_않는_구독권이면_예외_발생(){
        //given
        final Long memberNo=1L;
        final Long subscriptionNo=1L;
        final String today= "20250812";

        when(subscriptionDeletionService.getIfDeletable(
                subscriptionNo,
                memberNo,
                today)
        )
        .thenThrow(new SubscriptionNotFoundException(ErrorCode.SUBSCRIPTION_NOT_FOUND))
        ;

        //when
        assertThatThrownBy(()->target.cancel(memberNo,subscriptionNo,today))
                .isInstanceOf(SubscriptionNotFoundException.class);

        verify(subscriptionRepository, times(0))
                .delete(any(Subscription.class));
    }


    @Test
    @DisplayName("구독권 취소 시 결제환불 실패로 인한 구독권 삭제 호출되지 않음")
    void 구독권_취소_시_구독권_검증_로직_내_검증되지않아_결제_환불_실패(){
        //given
        final Long memberNo=1L;
        final Long subscriptionNo=1L;
        final String today= "20250812";
        final SubscriptionPlan subscriptionPlan = SubscriptionPlan.PLAN_4900;

        Subscription subscription = Subscription.create(memberNo,subscriptionPlan);

        when(
                subscriptionDeletionService.getIfDeletable(
                        any(),
                        any(),
                        any()
                )
        )
                .thenReturn(subscription)
        ;
        doThrow(new PayNotFoundException(ErrorCode.PAY_NOT_FOUND))
                .when(payServicePortForSubscription)
                .refund(subscriptionNo);
                ;


        //when,then
        assertThatThrownBy(()->target.cancel(subscriptionNo,memberNo,today))
                .isInstanceOf(PayNotFoundException.class);
        verify(subscriptionRepository, times(0))
                .delete(any(Subscription.class));
    }

    @Test
    @DisplayName("구독권 삭제 성공되어 구독권 삭제 및 결제환불로직 호출됨")
    void cancel_구독권_삭제_성공(){
        //given
        final Long memberNo=1L;
        final Long subscriptionNo=1L;
        final String today= "20250812";
        final SubscriptionPlan subscriptionPlan = SubscriptionPlan.PLAN_4900;

        Subscription subscription = Subscription.create(memberNo,subscriptionPlan);

        when(
                subscriptionDeletionService.getIfDeletable(
                        any(),
                        any(),
                        any()
                )
        )
                .thenReturn(subscription)
        ;

        //when
        target.cancel(subscriptionNo,memberNo,today);

        //then
        verify(subscriptionRepository, times(1))
                .delete(any(Subscription.class));
        verify(payServicePortForSubscription, times(1))
                .refund(any());
    }
}