package com.sunghyun.football.domain.noti.infrastructure.repository;

import com.sunghyun.football.domain.noti.infrastructure.entity.MatchFreeSubNotiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringJpaMatchFreeSubNotiRepository extends JpaRepository<MatchFreeSubNotiEntity,Long> {
    Optional<MatchFreeSubNotiEntity> findByEmailAndMatchNo(String email,Long matchNo);
}
