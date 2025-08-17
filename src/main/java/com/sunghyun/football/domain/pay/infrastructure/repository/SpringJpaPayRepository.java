package com.sunghyun.football.domain.pay.infrastructure.repository;

import com.sunghyun.football.domain.pay.infrastructure.entity.PayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringJpaPayRepository extends JpaRepository<PayEntity,Long> {
    PayEntity findByMemberNo(final Long memberNo);
    PayEntity findByMemberNoAndCreatedDt(final Long memberNo,final String createdDt);
    PayEntity findBySubscriptionNo(final Long subscriptionNo);
}
