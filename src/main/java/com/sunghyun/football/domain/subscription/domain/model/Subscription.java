package com.sunghyun.football.domain.subscription.domain.model;

import com.sunghyun.football.global.aop.Auditable;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.subscription.NoRemainingSubscriptionCountException;
import com.sunghyun.football.global.utils.MatchDateUtils;
import lombok.*;

@ToString
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Subscription extends Auditable {
    private Long subscriptionNo;

    private Long memberNo;

    private SubscriptionPlan subscriptionPlan;

    private SubscriptionStatus subscriptionStatus;

    private int remainingCount;

    private String startDt;

    private String endDt;


    public static Subscription create(
            final Long memberNo,
            final SubscriptionPlan subscriptionPlan
    ) {
        //생성 전 검증은 도메인 서비스에서 선진행
        final String nowDt = MatchDateUtils.getNowDtStr();
        final String endDt = MatchDateUtils.plusOneMonth(nowDt);

        return Subscription.builder()
                .memberNo(memberNo)
                .subscriptionPlan(subscriptionPlan)
                .remainingCount(subscriptionPlan.getLimit())
                .startDt(nowDt)
                .endDt(endDt)
                .subscriptionStatus(SubscriptionStatus.STAY)
                .build()
                ;
    }

    // DB 조회 또는 테스트에서 복원
    public static Subscription rehydrate(
            final Long subscriptionNo,
            final Long memberNo,
            final SubscriptionPlan subscriptionPlan,
            final SubscriptionStatus subscriptionStatus,
            final int remainingCount,
            final String startDt,
            final String endDt,
            final String createdDt,
            final String createdTm
            ) {
        Subscription subscription = new Subscription(subscriptionNo, memberNo, subscriptionPlan, subscriptionStatus, remainingCount, startDt, endDt);
        subscription.setCreatedDt(createdDt);
        subscription.setCreatedTm(createdTm);
        return subscription;
    }


    public void use() {
        if(remainingCount<=0){
            throw new NoRemainingSubscriptionCountException(ErrorCode.NO_SUBSCRIPTION_REMAINING);
        }

        remainingCount--;
    }

    public boolean hasBeenUsed() {
        return this.getSubscriptionPlan().getLimit() != this.remainingCount;
    }

    public void cancel() {
        this.subscriptionStatus = SubscriptionStatus.CANCEL;
    }
}
