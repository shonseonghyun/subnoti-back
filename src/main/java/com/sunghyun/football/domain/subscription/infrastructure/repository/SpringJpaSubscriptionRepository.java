package com.sunghyun.football.domain.subscription.infrastructure.repository;

import com.sunghyun.football.domain.subscription.domain.model.SubscriptionStatus;
import com.sunghyun.football.domain.subscription.infrastructure.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringJpaSubscriptionRepository extends JpaRepository<SubscriptionEntity,Long>{


    @Query("""
        SELECT s FROM SubscriptionEntity s
        WHERE s.memberNo = :memberNo
        AND :today BETWEEN s.startDt AND s.endDt
    """)
    SubscriptionEntity findValidByMemberNo(@Param("memberNo") final Long memberNo, @Param("today") final String today);

    @Query("""
        SELECT s FROM SubscriptionEntity s
        WHERE s.memberNo = :memberNo
        AND s.subscriptionNo=:subscriptionNo
        AND :today BETWEEN s.startDt AND s.endDt
    """)
    SubscriptionEntity findValidByMemberNoAndSubscriptionNo(@Param("memberNo") final Long memberNo,@Param("subscriptionNo") final Long subscriptionNo, @Param("today") final String today);

    @Query("""
        SELECT s FROM SubscriptionEntity s
        WHERE s.memberNo = :memberNo
        AND s.subscriptionStatus= :subscriptionStatus
        AND :today BETWEEN s.startDt AND s.endDt
    """)
    SubscriptionEntity findSubscriptionByMemberNoAndTodayAndStatus(@Param("memberNo") final Long memberNo,@Param("today") final String today, @Param("subscriptionStatus") final SubscriptionStatus subscriptionStatus);
}
