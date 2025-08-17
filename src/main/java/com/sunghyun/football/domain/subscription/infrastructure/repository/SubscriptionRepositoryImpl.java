package com.sunghyun.football.domain.subscription.infrastructure.repository;

import com.sunghyun.football.domain.subscription.domain.model.Subscription;
import com.sunghyun.football.domain.subscription.domain.model.SubscriptionStatus;
import com.sunghyun.football.domain.subscription.domain.repository.SubscriptionRepository;
import com.sunghyun.football.domain.subscription.infrastructure.entity.SubscriptionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SubscriptionRepositoryImpl implements SubscriptionRepository {
    private final SpringJpaSubscriptionRepository springJpaSubscriptionRepository;


    @Override
    public Optional<Subscription> findValidSubscriptionByMemberNoAndToday(final Long memberNo,final String today) {
        return Optional.ofNullable(springJpaSubscriptionRepository.findValidByMemberNo(memberNo,today))
                .map(SubscriptionEntity::toDomain);
    }

    @Override
    public Subscription save(final Subscription subscription) {
        return springJpaSubscriptionRepository.save(SubscriptionEntity.of(subscription)).toDomain();
    }

    @Override
    public Optional<Subscription> findValidSubscriptionBySubscriptionNoAndMemberNoAndToday(final Long memberNo,final Long subscriptionNo,String today) {
        return Optional.ofNullable(springJpaSubscriptionRepository.findValidByMemberNoAndSubscriptionNo(memberNo,subscriptionNo,today))
                .map(SubscriptionEntity::toDomain);
    }

    @Override
    public void delete(Subscription subscription) {
        springJpaSubscriptionRepository.delete(SubscriptionEntity.of(subscription));
    }

    @Override
    public void deleteAll() {
        springJpaSubscriptionRepository.deleteAll();
    }

    @Override
    public List<Subscription> findAll(){
        return springJpaSubscriptionRepository.findAll().stream()
                .map(SubscriptionEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Subscription> findSubscriptionByMemberNoAndTodayAndStatus(final Long memberNo, final String today, final SubscriptionStatus subscriptionStatus) {
        return Optional.ofNullable(springJpaSubscriptionRepository.findSubscriptionByMemberNoAndTodayAndStatus(memberNo,today,subscriptionStatus))
                .map(SubscriptionEntity::toDomain);
    }
}
