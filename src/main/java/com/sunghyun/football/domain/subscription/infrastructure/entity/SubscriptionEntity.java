package com.sunghyun.football.domain.subscription.infrastructure.entity;

import com.sunghyun.football.domain.subscription.domain.model.Subscription;
import com.sunghyun.football.domain.subscription.domain.model.SubscriptionPlan;
import com.sunghyun.football.domain.subscription.domain.model.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionNo;

    private Long memberNo;

    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SubscriptionStatus subscriptionStatus;

    private int remainingCount;

    private String startDt;

    private String endDt;

    private String createdDt;

    private String createdTm;

    public static SubscriptionEntity of(Subscription subscription) {
        if (subscription == null) {
            return null;
        }

        return SubscriptionEntity.builder()
                .subscriptionNo(subscription.getSubscriptionNo()) //신규 생성이면 null일 수 있음
                .memberNo(subscription.getMemberNo())
                .subscriptionPlan(subscription.getSubscriptionPlan())
                .subscriptionStatus(subscription.getSubscriptionStatus())
                .remainingCount(subscription.getRemainingCount())
                .startDt(subscription.getStartDt())
                .endDt(subscription.getEndDt())
                .createdDt(subscription.getCreatedDt())
                .createdTm(subscription.getCreatedTm())
                .build();
    }

    public Subscription toDomain(){
        return Subscription.rehydrate(
                this.subscriptionNo,
                this.memberNo,
                this.subscriptionPlan,
                this.subscriptionStatus,
                this.remainingCount,
                this.startDt,
                this.endDt,
                this.createdDt,
                this.createdTm
        );
    }
}
