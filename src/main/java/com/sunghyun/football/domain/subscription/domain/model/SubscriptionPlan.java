package com.sunghyun.football.domain.subscription.domain.model;

import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.subscription.exception.InvalidSubscriptionAmountException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SubscriptionPlan {
    PLAN_9900("무제한 등록제",9900,Integer.MAX_VALUE),
    PLAN_4900("중간 등록제",4900,5),
    PLAN_990("맛보기 등록제",990,1)
    ;

    private final String desc;
    private final int amount;
    private final int limit;

    public static SubscriptionPlan getPlanByAmount(final int amount) {
        return Arrays.stream(SubscriptionPlan.values())
                .filter(payPlan-> payPlan.getAmount()==amount)
                .findFirst()
                .orElseThrow(()-> new InvalidSubscriptionAmountException(ErrorType.INVALID_AMOUNT))
                ;
    }
}
