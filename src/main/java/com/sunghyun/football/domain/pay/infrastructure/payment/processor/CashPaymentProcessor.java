package com.sunghyun.football.domain.pay.infrastructure.payment.processor;

import com.sunghyun.football.domain.pay.domain.model.Pay;
import com.sunghyun.football.domain.pay.domain.model.PaymentMethod;
import com.sunghyun.football.domain.pay.domain.service.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CashPaymentProcessor implements PaymentProcessor {
    @Override
    public void pay() {
        log.info("현금 결제 진행");
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.CASH;
    }

    @Override
    public void refund(Pay pay) {
        log.info("현금 결제 환불 진행");
    }
}
