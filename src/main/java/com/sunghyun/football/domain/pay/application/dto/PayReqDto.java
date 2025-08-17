package com.sunghyun.football.domain.pay.application.dto;

import com.sunghyun.football.domain.pay.domain.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PayReqDto {
    private Long memberNo;
    private int amount;
    private PaymentMethod paymentMethod;
}
