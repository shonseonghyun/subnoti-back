package com.sunghyun.football.domain.pay.application;

import com.sunghyun.football.domain.pay.application.port.out.SubscriptionServicePortForPay;
import com.sunghyun.football.domain.pay.domain.model.Pay;
import com.sunghyun.football.domain.pay.domain.model.PaymentMethod;
import com.sunghyun.football.domain.pay.domain.repository.PayRepository;
import com.sunghyun.football.domain.pay.domain.service.PayCreationService;
import com.sunghyun.football.domain.pay.domain.service.PaymentBuilderFactory;
import com.sunghyun.football.domain.pay.domain.service.PaymentProcessor;
import com.sunghyun.football.global.utils.MatchDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayService {
    private final PayCreationService payCreationService;
    private final PaymentBuilderFactory paymentBuilderFactory;
    private final PayRepository payRepository;
    private final SubscriptionServicePortForPay subscriptionServicePort;

    @Transactional
    public void pay(final Long memberNo,final int amount,final PaymentMethod paymentMethod) {
        logTransaction("pay - start");

        //구독권 검증 및 생성
        Long subscriptionNo = subscriptionServicePort.createSubscription(memberNo,amount,MatchDateUtils.getNowDtStr());

        //지불수단 담당 객체
        PaymentProcessor paymentProcessor = paymentBuilderFactory.getPaymentProcessor(paymentMethod);

        //도메인 생성
        Pay pay = payCreationService.createPayDomain(memberNo,subscriptionNo,amount,paymentMethod);

        //결제
        paymentProcessor.pay();

        //도메인 내부 결제 성공 상태변경
        pay.pay();

        //도메인 저장
        payRepository.save(pay);
    }

    private void logTransaction(String method) {
        log.info("{} | transaction active: {}, name: {}",
                method,
                TransactionSynchronizationManager.isActualTransactionActive(),
                TransactionSynchronizationManager.getCurrentTransactionName()
        );
    }
}
