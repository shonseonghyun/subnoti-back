package com.sunghyun.football.domain.subscription.application.service;

import com.sunghyun.football.domain.subscription.application.service.port.out.PayServicePortForSubscription;
import com.sunghyun.football.domain.subscription.domain.model.Subscription;
import com.sunghyun.football.domain.subscription.domain.repository.SubscriptionRepository;
import com.sunghyun.football.domain.subscription.domain.service.SubscriptionDeletionService;
import com.sunghyun.football.global.exception.subscription.SubscriptionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionDeletionService subscriptionDeletionService;
    private final SubscriptionRepository subscriptionRepository;
    private final PayServicePortForSubscription payServicePortForSubscription;


    @Transactional
    public void cancel(final Long memberNo,final Long subscriptionNo,final String today) {
        Subscription subscription ;

        try{
            //구독권 취소
            subscription = subscriptionDeletionService.getIfDeletable(memberNo,subscriptionNo,today);
        }catch (SubscriptionException e){
            log.warn("[구독권 삭제 실패] memberNo={}, subscriptionNo={}, reason={}", memberNo, subscriptionNo, e.getMessage());
            throw e;
        }

        //2. 결제 환불 진행
        payServicePortForSubscription.refund(subscriptionNo);


        //구독권 취소 반영
        subscriptionRepository.delete(subscription);
    }
}
