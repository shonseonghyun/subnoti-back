package com.sunghyun.football.domain.subscription.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubscriptionStatus {
    STAY("구독 중"),
    CANCEL("구독 취소")
    ;

    private final String desc;
}
