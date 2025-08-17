package com.sunghyun.football.domain.pay.infrastructure.repository;

import com.sunghyun.football.domain.pay.domain.model.Pay;
import com.sunghyun.football.domain.pay.domain.repository.PayRepository;
import com.sunghyun.football.domain.pay.infrastructure.entity.PayEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PayRepositoryImpl implements PayRepository {
    private final SpringJpaPayRepository springJpaPayRepository;

    @Override
    public Optional<Pay> findByMemberNoAndCreatedDt(final Long memberNo,final String createdDt) {
        return Optional.ofNullable(springJpaPayRepository.findByMemberNoAndCreatedDt(memberNo,createdDt))
                .map(PayEntity::toDomain);
    }

    @Override
    public Pay save(final Pay pay) {
        return springJpaPayRepository.save(PayEntity.of(pay)).toDomain();
    }

    @Override
    public Optional<Pay> findBySubscriptionNo(final Long subscriptionNo) {
        return Optional.ofNullable(springJpaPayRepository.findBySubscriptionNo(subscriptionNo))
                .map(PayEntity::toDomain);
    }

    @Override
    public void deleteAll() {
        springJpaPayRepository.deleteAll();
    }

    @Override
    public List<Pay> findAll(){
        return springJpaPayRepository.findAll().stream()
                .map(PayEntity::toDomain)
                .toList();
    }
}
