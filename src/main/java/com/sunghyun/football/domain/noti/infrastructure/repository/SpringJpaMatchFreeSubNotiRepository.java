package com.sunghyun.football.domain.noti.infrastructure.repository;

import com.sunghyun.football.domain.noti.infrastructure.entity.MatchFreeSubNotiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringJpaMatchFreeSubNotiRepository extends JpaRepository<MatchFreeSubNotiEntity,Long> {
    List<MatchFreeSubNotiEntity> findByEmailAndMatchNo(String email, Long matchNo);
}
