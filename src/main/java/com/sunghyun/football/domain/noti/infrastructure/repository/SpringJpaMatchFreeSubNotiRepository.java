package com.sunghyun.football.domain.noti.infrastructure.repository;

import com.sunghyun.football.domain.noti.infrastructure.entity.FreeSubNotiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringJpaMatchFreeSubNotiRepository extends JpaRepository<FreeSubNotiEntity,Long> {
    List<FreeSubNotiEntity> findByEmailAndMatchNo(String email, Long matchNo);
}
