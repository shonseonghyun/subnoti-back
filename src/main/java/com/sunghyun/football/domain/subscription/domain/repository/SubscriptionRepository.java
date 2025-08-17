package com.sunghyun.football.domain.subscription.domain.repository;

import com.sunghyun.football.domain.subscription.domain.model.Subscription;
import com.sunghyun.football.domain.subscription.domain.model.SubscriptionStatus;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {
    Optional<Subscription> findValidSubscriptionByMemberNoAndToday(final Long memberNo,final String today);
    Subscription save(final Subscription subscription);
    Optional<Subscription> findValidSubscriptionBySubscriptionNoAndMemberNoAndToday(final Long memberNo,final Long subscriptionNo,final String today);
    void delete(Subscription subscription);
    void deleteAll();
    List<Subscription> findAll();
    Optional<Subscription> findSubscriptionByMemberNoAndTodayAndStatus(final Long memberNo,final String today,final SubscriptionStatus subscriptionStatus);
}
