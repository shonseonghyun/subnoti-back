package com.sunghyun.football.domain.subscription.domain.service;

import com.sunghyun.football.domain.subscription.domain.model.Subscription;
import com.sunghyun.football.domain.subscription.domain.model.SubscriptionStatus;
import com.sunghyun.football.domain.subscription.domain.repository.SubscriptionRepository;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.subscription.SubscriptionAlreadyCancelException;
import com.sunghyun.football.global.exception.exceptions.subscription.SubscriptionAlreadyUsedException;
import com.sunghyun.football.global.exception.exceptions.subscription.SubscriptionNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionDeletionServiceTest {

    @InjectMocks
    private SubscriptionDeletionService target;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Test
    @DisplayName("getIfDeletable: 조건에 맞는 구독권이 없으면 SubscriptionNotFoundException 발생")
    void getIfDeletable_whenSubscriptionNotFound_thenThrowSubscriptionNotFoundException() {
        //given
        final Long memberNo = 1L;
        final Long subscriptionNo = 1L;
        final String today = "20250909";

        when(subscriptionRepository.findValidSubscriptionBySubscriptionNoAndMemberNoAndToday(
                memberNo, subscriptionNo, today))
                .thenThrow(new SubscriptionNotFoundException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        //when,then
        Assertions.assertThatThrownBy(()->target.getIfDeletable(memberNo,subscriptionNo,today))
                        .isInstanceOf(SubscriptionNotFoundException.class);
    }

    @Test
    @DisplayName("getIfDeletable: 이미 사용한 구독권이면 SubscriptionAlreadyUsedException 발생")
    void getIfDeletable_whenSubscriptionAlreadyUsed_thenThrowSubscriptionAlreadyUsedException() {
        //given
        final Long memberNo = 1L;
        final Long subscriptionNo = 1L;
        final String today = "20250909";

        Subscription subscription = mock(Subscription.class);

        when(subscriptionRepository.findValidSubscriptionBySubscriptionNoAndMemberNoAndToday(memberNo,subscriptionNo,today))
               .thenReturn(Optional.of(subscription))
               ;
        when(subscription.hasBeenUsed()).thenReturn(true);

        //when,then
        Assertions.assertThatThrownBy(()->target.getIfDeletable(memberNo,subscriptionNo,today))
                .isInstanceOf(SubscriptionAlreadyUsedException.class)
                ;
    }

    @Test
    @DisplayName("getIfDeletable: 이미 전에 구독권을 취소한 적이 있다면 SubscriptionAlreadyCancelException 발생")
    void getIfDeletable_whenSubscriptionAlreadyCancel_thenThrowSubscriptionAlreadyCancelException() {
        //given
        final Long memberNo = 1L;
        final Long subscriptionNo = 1L;
        final String today = "20250909";

        Subscription subscription = mock(Subscription.class);

        when(subscriptionRepository.findValidSubscriptionBySubscriptionNoAndMemberNoAndToday(memberNo,subscriptionNo,today))
                .thenReturn(Optional.of(subscription));
        when(subscription.hasBeenUsed()).thenReturn(false);
        when(subscriptionRepository.findSubscriptionByMemberNoAndTodayAndStatus(memberNo,today, SubscriptionStatus.CANCEL))
                .thenReturn(Optional.of(subscription))
                ;


        //when,then
        Assertions.assertThatThrownBy(()->target.getIfDeletable(memberNo,subscriptionNo,today))
                .isInstanceOf(SubscriptionAlreadyCancelException.class)
        ;
    }

    @Test
    @DisplayName("getIfDeletable: 삭제 가능 구독권이면 도메인 반환")
    void getIfDeletable_whenSubscriptionDeletable_thenReturnSubscription(){
        //given
        final Long memberNo = 1L;
        final Long subscriptionNo = 1L;
        final String today = "20250909";

        Subscription subscription = mock(Subscription.class);

        when(subscriptionRepository.findValidSubscriptionBySubscriptionNoAndMemberNoAndToday(memberNo,subscriptionNo,today))
                .thenReturn(Optional.of(subscription))
        ;
        when(subscription.hasBeenUsed()).thenReturn(false);
        when(subscriptionRepository.findSubscriptionByMemberNoAndTodayAndStatus(memberNo,today, SubscriptionStatus.CANCEL))
                .thenReturn(Optional.empty())
        ;
        //when
        Subscription result = target.getIfDeletable(memberNo,subscriptionNo,today);

        //then
        assertThat(result).isNotNull();
    }
}