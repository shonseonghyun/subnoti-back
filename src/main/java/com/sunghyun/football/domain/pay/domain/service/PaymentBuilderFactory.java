package com.sunghyun.football.domain.pay.domain.service;

import com.sunghyun.football.domain.pay.domain.model.PaymentMethod;
import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.pay.exception.UnsupportedPaymentMethodException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class PaymentBuilderFactory {
    private final Map<PaymentMethod,PaymentProcessor> paymentProcessorMap = new HashMap<>();

    public PaymentBuilderFactory(List<PaymentProcessor> paymentProcessorList){
        for(PaymentProcessor paymentProcessor:paymentProcessorList){
            paymentProcessorMap.put(paymentProcessor.getPaymentMethod(),paymentProcessor);
        }
    }

    public PaymentProcessor getPaymentProcessor(final PaymentMethod paymentMethod) {
        PaymentProcessor paymentProcessor=paymentProcessorMap.get(paymentMethod);
        if(paymentProcessor==null){
            log.warn("존재하지 않는 지불수단입니다.");
            throw new UnsupportedPaymentMethodException(ErrorType.UNSUPPORTED_PAYMENT_METHOD);
        }

        return paymentProcessor;
    }
}
