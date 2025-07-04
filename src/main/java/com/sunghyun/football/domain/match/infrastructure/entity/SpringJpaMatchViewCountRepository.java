//package com.sunghyun.football.domain.match.infrastructure.entity;
//
//import jakarta.persistence.LockModeType;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Lock;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.Optional;
//
//public interface SpringJpaMatchViewCountRepository extends JpaRepository<MatchViewCountEntity,Long> {
//
//    @Lock(LockModeType.OPTIMISTIC)
//    @Query("select m from MatchViewCountEntity m where m.viewNo=:matchNo")
//    Optional<MatchViewCountEntity> findMatchViewCountByMatchNoOptimistic(@Param("matchNo") Long matchNo);
//
//}
