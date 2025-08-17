package com.sunghyun.football.domain.subscription.domain.service;

import com.sunghyun.football.domain.subscription.domain.model.Subscription;
import com.sunghyun.football.domain.subscription.domain.model.SubscriptionPlan;
import com.sunghyun.football.domain.subscription.domain.repository.SubscriptionRepository;
import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.subscription.exception.SubscriptionAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionCreationService {

    private final SubscriptionRepository subscriptionRepository;

    public Subscription createSubscriptionDomain(final Long memberNo,final int amount,final String today) {
        //요금제 금액 검증
        SubscriptionPlan subscriptionPlan = SubscriptionPlan.getPlanByAmount(amount);

        //당월 구독권 존재 이력 검증
        subscriptionRepository.findValidSubscriptionByMemberNoAndToday(memberNo, today)
                .ifPresent(subscription -> {
                    throw new SubscriptionAlreadyExistsException(ErrorType.SUBSCRIPTION_ALREADY_EXIST_THIS_MONTH);
                });

        return Subscription.create(memberNo,subscriptionPlan);
    }
}
