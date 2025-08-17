package com.sunghyun.football.domain.subscription.domain.model;

import com.sunghyun.football.global.exception.subscription.exception.NoRemainingSubscriptionCountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class SubscriptionTest {
    
    @Test
    @DisplayName("구독권 도메인 생성 시 멤버변수들 정상 주입 확인")
    void 구독권_생성_성공(){
        //given
        final Long memberNo = 1L;
        final SubscriptionPlan subscriptionPlan = SubscriptionPlan.PLAN_4900;

        //when
        Subscription subscription = Subscription.create(memberNo,subscriptionPlan);

        //given
        assertThat(subscription.getSubscriptionNo()).isNull();
        assertThat(subscription.getMemberNo()).isEqualTo(memberNo);
        assertThat(subscription.getSubscriptionPlan()).isEqualTo(subscriptionPlan);
        assertThat(subscription.getSubscriptionStatus()).isEqualTo(SubscriptionStatus.STAY);
        assertThat(subscription.getRemainingCount()).isEqualTo(subscriptionPlan.getLimit());
    }

    @Test
    @DisplayName("구독권 사용 시 잔여 횟수 0인 경우 예외 응답")
    void 구독권_잔여_횟수_0인_경우_예외_응답(){
        // given
        Subscription subscription = Subscription.rehydrate(
                1L,
                10L,
                SubscriptionPlan.PLAN_990,
                SubscriptionStatus.STAY,
                0,
                "20250808",
                "20250908",
                "20250808",
                "000000"

        );

        // when & then
        assertThatThrownBy(subscription::use)
                .isInstanceOf(NoRemainingSubscriptionCountException.class);
    }

    @Test
    @DisplayName("구독권 사용 시 1 감소 검증")
    void 구독권_잔여_횟수_남았을_시_사용하면_1_감소(){
        // given
        final Long memberNo = 1L;
        final SubscriptionPlan subscriptionPlan = SubscriptionPlan.PLAN_990;
        Subscription subscription = Subscription.create(memberNo,subscriptionPlan);

        // when
        subscription.use();

        //then
        assertThat(subscription.getRemainingCount()).isEqualTo(subscriptionPlan.getLimit()-1);
    }

    @Test
    @DisplayName("구독권 사용 이력 있는 경우 true 리턴")
    void 구독권_사용_이력_있는_경우_true_리턴(){
        // given
        final Long memberNo = 1L;
        final SubscriptionPlan subscriptionPlan = SubscriptionPlan.PLAN_990;
        Subscription subscription = Subscription.create(memberNo,subscriptionPlan);
        subscription.use();

        //then
        final boolean hasBeenUsedFlg = subscription.hasBeenUsed();
        assertThat(hasBeenUsedFlg).isEqualTo(true);
    }

    @Test
    @DisplayName("구독권 취소 시 취소(cancel) 상태로 변경")
    void 구독권_취소_시_구독권_상태_cancel_변경(){
        //given
        final Long memberNo = 1L;
        final SubscriptionPlan subscriptionPlan = SubscriptionPlan.PLAN_990;
        Subscription subscription = Subscription.create(memberNo,subscriptionPlan);

        //when
        subscription.cancel();

        //then
        assertThat(subscription.getSubscriptionStatus()).isEqualTo(SubscriptionStatus.CANCEL);
    }

}