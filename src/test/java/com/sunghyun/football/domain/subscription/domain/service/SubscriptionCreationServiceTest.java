package com.sunghyun.football.domain.subscription.domain.service;

import com.sunghyun.football.domain.subscription.domain.model.Subscription;
import com.sunghyun.football.domain.subscription.domain.model.SubscriptionPlan;
import com.sunghyun.football.domain.subscription.domain.repository.SubscriptionRepository;
import com.sunghyun.football.global.exception.subscription.exception.InvalidSubscriptionAmountException;
import com.sunghyun.football.global.exception.subscription.exception.SubscriptionAlreadyExistsException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SubscriptionCreationServiceTest {

    @InjectMocks
    private SubscriptionCreationService target;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private SubscriptionPlan subscriptionPlan;

    @Test
    @DisplayName("요금제 금액 검증 실패 시 예외 응답")
    void 요금제_금액_검증_실패_시_예외_응답(){
        //given
        final Long memberNo=1L;
        final int wrongAmount = 99;
        final String today = "20250807";

        //when,then
        Assertions.assertThatThrownBy(()->target.createSubscriptionDomain(memberNo,wrongAmount,today))
                .isInstanceOf(InvalidSubscriptionAmountException.class);
    }

    @Test
    @DisplayName("같은 달 요금제 중복 결제 시 예외 응답")
    void 특정_달_요금제_중복_결제_시_예외_응답(){
        //given
        final Long memberNo= 1L;
        final int amount = 9900;
        final SubscriptionPlan subscriptionPlan9900 = SubscriptionPlan.PLAN_9900;
        final String today = "20250807";

        Subscription dummySubscription = Subscription.create(memberNo,subscriptionPlan9900);

        doReturn(Optional.ofNullable(dummySubscription))
                .when(subscriptionRepository).findValidSubscriptionByMemberNoAndToday(memberNo,today);

        //when,then
        Assertions.assertThatThrownBy(()->target.createSubscriptionDomain(memberNo,amount,today))
                .isInstanceOf(SubscriptionAlreadyExistsException.class)
        ;
    }

    @Test
    @DisplayName("Subscription 도메인 생성 성공")
    void Subscription_도메인_생성_성공(){
        //given
        final Long memberNo= 1L;
        final int amount = 9900;
        final String today = "20250807";

        //when
        Subscription subscription = target.createSubscriptionDomain(memberNo,amount,today);

        //then
        Assertions.assertThat(subscription).isNotNull();
    }

}