package com.sunghyun.football.domain.subscription.domain.service;

import com.sunghyun.football.domain.subscription.domain.model.Subscription;
import com.sunghyun.football.domain.subscription.domain.model.SubscriptionStatus;
import com.sunghyun.football.domain.subscription.domain.repository.SubscriptionRepository;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.subscription.SubscriptionAlreadyCancelException;
import com.sunghyun.football.global.exception.exceptions.subscription.SubscriptionAlreadyUsedException;
import com.sunghyun.football.global.exception.exceptions.subscription.SubscriptionNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 구독권 삭제 관련 도메인 서비스
 *
 * <p>SubscriptionDeletionService는 구독권 삭제 정책을 검증하고, 삭제 가능 여부에 따라 도메인을 반환합니다.
 * 실제 삭제 로직은 Repository나 상위 서비스에서 수행하며, 이 서비스는 정책 검증만 담당합니다.</p>
 *
 * 주요 역할:
 * 1. 구독권 존재 여부 확인
 * 2. 구독권 사용 여부 검증
 * 3. 삭제 가능한 구독권 도메인 반환
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionDeletionService {

    private final SubscriptionRepository subscriptionRepository;

    //삭제 검증 후 도메인 반환
    public Subscription getIfDeletable(final Long memberNo,final Long subscriptionNo,final String today){
        //1. 회원번호+구독권번호+구독권유효기간 포함에 부합하는지 검증
        Subscription subscription = subscriptionRepository.findValidSubscriptionBySubscriptionNoAndMemberNoAndToday(memberNo,subscriptionNo, today)
                .orElseThrow(()->new SubscriptionNotFoundException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        //2. 구독권을 한 번이라도 사용한 적 있는지 검증
        if(subscription.hasBeenUsed()){
            throw new SubscriptionAlreadyUsedException(ErrorCode.SUBSCRIPTION_ALREADY_USE);
        }

        //3. 1회 이상 구독권 환불 진행한 적 있는지 검증
        subscriptionRepository.findSubscriptionByMemberNoAndTodayAndStatus(memberNo,today, SubscriptionStatus.CANCEL)
                .ifPresent(domain->{
                    System.out.println("존재해");
                    throw new SubscriptionAlreadyCancelException(ErrorCode.SUBSCRIPTION_ALREADY_CANCELED);
                })
        ;

        return subscription;
    }

}
