package com.sunghyun.football.domain.match.infrastructure;

import com.sunghyun.football.domain.match.infrastructure.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringJpaMatchRepository extends JpaRepository<MatchEntity,Long> {
    Optional<MatchEntity> findByMatchNo(Long matchNo);
    List<MatchEntity> findAllByStartDt(String startDt);
}
