package com.sunghyun.football.domain.pay.domain;

import com.sunghyun.football.domain.subscription.domain.model.SubscriptionPlan;
import com.sunghyun.football.global.exception.subscription.exception.InvalidSubscriptionAmountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubscriptionPlanTest {

    @Test
    @DisplayName("올바르지 않은 금액 들어온 경우, 에러 응답")
    void fail(){
        int wrongAmount = 9;

        assertThatThrownBy(()-> SubscriptionPlan.getPlanByAmount(9))
                .isInstanceOf(InvalidSubscriptionAmountException.class);

    }

    @Test
    @DisplayName("plan에 존재하는 금액 들어온 경우, 해당 enum 반환")
    void success(){
        //given
        int planAmount = 990;

        //when
        SubscriptionPlan subscriptionPlan = SubscriptionPlan.getPlanByAmount(planAmount);

        assertThat(subscriptionPlan).isEqualTo(SubscriptionPlan.PLAN_990);
    }

}