package com.sunghyun.football.domain.noti.domain.repository;

import com.sunghyun.football.domain.noti.domain.FreeSubNoti;
import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;

import java.util.List;
import java.util.Optional;

public interface FreeSubNotiRepository {
    FreeSubNoti save(FreeSubNoti freeSubNoti);
    List<FreeSubType> getFreeSubTypes(String email, Long matchNo);
    List<FreeSubNoti> getFreeSubNoties(Long memberNo,String nowDt);
    void delFreeSubNoti(Long notiNo);
    Optional<Long> getMaxNotiNo(String startDt);
    Optional<Long> getMinNotiNo(String startDt);
}
