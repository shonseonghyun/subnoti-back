package com.sunghyun.football.domain.pay.domain.model;

import com.sunghyun.football.global.aop.Auditable;
import lombok.*;

@Setter(AccessLevel.PRIVATE)
@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Pay extends Auditable {
    private Long payNo;
    private Long memberNo;
    private Long subscriptionNo;
    private int amount;
    private PaymentMethod paymentMethod;
    private PayStatus payStatus;


    /**
     * Pay 도메인 객체 생성을 위한 팩토리 메서드.
     * 해당 메서드는 도메인 서비스(PayCreationService) 외에는 호출하지 말 것.
     */
    public static Pay create(final Long memberNo, final Long subscriptionNo, final int amount, final PaymentMethod paymentMethod) {
        //생성 전 검증은 도메인 서비스에서 선진행
        return Pay.builder()
                .memberNo(memberNo)
                .subscriptionNo(subscriptionNo)
                .amount(amount)
                .paymentMethod(paymentMethod)
                .payStatus(PayStatus.PENDING)
                .build();
    }

    //DB 상태로부터 재구성(검증 생략)
    public static Pay rehydrate(
            final Long payNo,
            final Long memberNo,
            final Long subscriptionNo,
            final int amount,
            final PaymentMethod paymentMethod,
            final PayStatus payStatus,
            final String createdDt,
            final String createdTm
    )
    {
        Pay pay = Pay.builder()
                .payNo(payNo)
                .memberNo(memberNo)
                .subscriptionNo(subscriptionNo)
                .amount(amount)
                .payStatus(payStatus)
                .paymentMethod(paymentMethod)
                .build();
        pay.setCreatedDt(createdDt);
        pay.setCreatedTm(createdTm);

        return pay;
    }

    public void pay() {
        this.payStatus= PayStatus.SUCCESS;
    }

    public void refund() {
        this.payStatus= PayStatus.REFUND;
    }
}
