package com.sunghyun.football.domain.pay.application.port.out;

import com.sunghyun.football.domain.pay.domain.model.Pay;
import com.sunghyun.football.domain.pay.domain.repository.PayRepository;
import com.sunghyun.football.domain.pay.domain.service.PaymentBuilderFactory;
import com.sunghyun.football.domain.pay.domain.service.PaymentProcessor;
import com.sunghyun.football.domain.subscription.application.service.port.out.PayServicePortForSubscription;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.pay.PayNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayAdapter implements PayServicePortForSubscription {
    private final PayRepository payRepository;
    private final PaymentBuilderFactory paymentBuilderFactory;

    @Override
    @Transactional
    public void refund(final Long subscriptionNo){
        //존재여부 검증
        Pay pay = payRepository.findBySubscriptionNo(subscriptionNo)
                .orElseThrow(()->new PayNotFoundException(ErrorCode.PAY_NOT_FOUND));

        //지불수단 담당 객체
        PaymentProcessor paymentProcessor = paymentBuilderFactory.getPaymentProcessor(pay.getPaymentMethod());

        //담당 객체를 통한 결제 환불 진행
        paymentProcessor.refund(pay);

        //도메인 내부 결제 환불 상태변경
        pay.refund();

        //결과 저장
        payRepository.save(pay);
    }
}
