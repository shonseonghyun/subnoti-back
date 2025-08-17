package com.sunghyun.football.domain.pay.domain.service;

import com.sunghyun.football.domain.pay.domain.model.Pay;
import com.sunghyun.football.domain.pay.domain.model.PaymentMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PayCreationService {

    public Pay createPayDomain(final Long memberNo,
                               final Long subscriptionNo,
                               final int amount,
                               final PaymentMethod paymentMethod)
    {
        //payMethod 유효성 검증
        paymentMethod.checkAvailable();

        return Pay.create(memberNo,subscriptionNo,amount,paymentMethod);
    }
}
