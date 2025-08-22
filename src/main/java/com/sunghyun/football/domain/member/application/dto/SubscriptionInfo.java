package com.sunghyun.football.domain.member.application.dto;

import com.sunghyun.football.domain.enumMapper.enums.EnumMapperValue;
import com.sunghyun.football.domain.subscription.domain.model.Subscription;
import com.sunghyun.football.domain.subscription.domain.model.SubscriptionStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionInfo {
    private Long subscriptionNo;

    private Long memberNo;

    private EnumMapperValue subscriptionPlan;

    private SubscriptionStatus subscriptionStatus;

    private int remainingCount;

    private String startDt;

    private String endDt;

    public static SubscriptionInfo from(Subscription subscription) {
        if (subscription == null) {
            return null;
        }

        return new SubscriptionInfo(
                subscription.getSubscriptionNo(),
                subscription.getMemberNo(),
                new EnumMapperValue(subscription.getSubscriptionPlan()),
                subscription.getSubscriptionStatus(),
                subscription.getRemainingCount(),
                subscription.getStartDt(),
                subscription.getEndDt()
        );
    }
}
