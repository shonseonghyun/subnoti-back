package com.sunghyun.football.domain.pay.domain.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PayStatus {
    PENDING("결제 대기"),
    SUCCESS("결제 성공"),
    REFUND("결제 환불")
    ;

    private final String desc;
}
