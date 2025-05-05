package com.sunghyun.football.domain.noti.infrastructure.repository;

import com.sunghyun.football.domain.noti.infrastructure.entity.FreeSubNotiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SpringJpaMatchFreeSubNotiRepository extends JpaRepository<FreeSubNotiEntity,Long> {
    List<FreeSubNotiEntity> findByEmailAndMatchNo(String email, Long matchNo);

    @Query("SELECT m FROM FreeSubNotiEntity m where m.memberNo = :memberNo and m.startDt >= :nowDt")
    List<FreeSubNotiEntity> findByMemberNoAndStartDtAfterToday(Long memberNo,String nowDt);

    @Query(value = "SELECT min(m.noti_No) FROM free_sub_noti_req m where m.start_Dt >= :startDt",nativeQuery = true)
    Optional<Long> findMinNotiNoByStartDt(String startDt);

    @Query(value = "SELECT max(m.noti_No) FROM free_sub_noti_req m where m.start_Dt >= :startDt",nativeQuery = true)
    Optional<Long> findMaxNotiNoByStartDt(String startDt);
}
