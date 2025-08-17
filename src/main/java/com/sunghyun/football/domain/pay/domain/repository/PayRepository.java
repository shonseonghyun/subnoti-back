package com.sunghyun.football.domain.pay.domain.repository;

import com.sunghyun.football.domain.pay.domain.model.Pay;

import java.util.List;
import java.util.Optional;

public interface PayRepository {
    Optional<Pay> findByMemberNoAndCreatedDt(final Long memberNo,final String createdDt);
    Pay save(final Pay pay);
    Optional<Pay> findBySubscriptionNo(final Long subscriptionNo);
    void deleteAll();
    List<Pay> findAll();
}
