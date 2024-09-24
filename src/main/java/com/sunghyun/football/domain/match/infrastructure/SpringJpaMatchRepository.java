package com.sunghyun.football.domain.match.infrastructure;

import com.sunghyun.football.domain.match.infrastructure.entity.MatchEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringJpaMatchRepository extends JpaRepository<MatchEntity,Long> {
    Optional<MatchEntity> findByMatchNo(Long matchNo);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from MatchEntity m where m.matchNo=:matchNo")
    Optional<MatchEntity> findByMatchNoPessimistic(@Param("matchNo") Long matchNo);

    List<MatchEntity> findAllByStartDt(String startDt);
}
