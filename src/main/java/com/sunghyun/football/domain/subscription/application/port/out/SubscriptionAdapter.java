package com.sunghyun.football.domain.subscription.application.port.out;

import com.sunghyun.football.domain.member.application.dto.SubscriptionInfo;
import com.sunghyun.football.domain.member.application.port.out.SubscriptionServicePortForMember;
import com.sunghyun.football.domain.noti.application.port.out.SubscriptionServicePortForNoti;
import com.sunghyun.football.domain.pay.application.port.out.SubscriptionServicePortForPay;
import com.sunghyun.football.domain.subscription.domain.model.Subscription;
import com.sunghyun.football.domain.subscription.domain.repository.SubscriptionRepository;
import com.sunghyun.football.domain.subscription.domain.service.SubscriptionCreationService;
import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.subscription.exception.SubscriptionNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

//Adappter로 명명-> 다른 도메인에서만 호출
//Service로 명명-> 외부 요청(Controller)으로부터 직접 호출

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionAdapter implements SubscriptionServicePortForPay, SubscriptionServicePortForNoti , SubscriptionServicePortForMember {

    private final SubscriptionCreationService subscriptionCreationService;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public Long createSubscription(final Long memberNo,final int amount,final String today){
        logTransaction("createSubscription");

        //구독권 생성
        Subscription subscription = subscriptionCreationService.createSubscriptionDomain(memberNo,amount,today);

        //구독권 저장
        Subscription savedSubscription = subscriptionRepository.save(subscription);

        return savedSubscription.getSubscriptionNo();
    }


    private void logTransaction(String method) {
        log.info("{} | transaction active: {}, name: {}",
                method,
                TransactionSynchronizationManager.isActualTransactionActive(),
                TransactionSynchronizationManager.getCurrentTransactionName()
        );
    }


    @Override
    @Transactional
    public void use(final Long memberNo,final String today) {
        //구독권 존재 여부 검증
        Subscription subscription = subscriptionRepository.findValidSubscriptionByMemberNoAndToday(memberNo,today)
                .orElseThrow(()->new SubscriptionNotFoundException(ErrorType.SUBSCRIPTION_NOT_FOUND));

        //구독권 사용
        subscription.use();

        //저장
        subscriptionRepository.save(subscription);
    }

    @Override
    @Transactional
    public SubscriptionInfo getSubscription(final Long memberNo,String today) {
        Subscription subscription = subscriptionRepository.findValidSubscriptionByMemberNoAndToday(memberNo,today)
                .orElse(null);

        return subscription == null ? null  : SubscriptionInfo.from(subscription);
    }
}
