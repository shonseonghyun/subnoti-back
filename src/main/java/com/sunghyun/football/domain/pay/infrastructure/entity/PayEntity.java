package com.sunghyun.football.domain.pay.infrastructure.entity;

import com.sunghyun.football.domain.pay.domain.model.Pay;
import com.sunghyun.football.domain.pay.domain.model.PayStatus;
import com.sunghyun.football.domain.pay.domain.model.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payNo;

    private Long memberNo;

    private Long subscriptionNo;

    private int amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;

    private String createdDt;

    private String createdTm;

    public static PayEntity of(final Pay pay) {
        if (pay == null) {
            return null;
        }

        return PayEntity.builder()
                .payNo(pay.getPayNo()) // 신규 생성이면 null일 수 있음
                .memberNo(pay.getMemberNo())
                .subscriptionNo(pay.getSubscriptionNo())
                .amount(pay.getAmount())
                .paymentMethod(pay.getPaymentMethod())
                .payStatus(pay.getPayStatus())
                .createdDt(pay.getCreatedDt())
                .createdTm(pay.getCreatedTm())
                .build();
    }

    public Pay toDomain(){
        return Pay.rehydrate(
                this.payNo,
                this.memberNo,
                this.subscriptionNo,
                this.amount,
                this.paymentMethod,
                this.payStatus,
                this.createdDt,
                this.createdTm
        );
    }
}
