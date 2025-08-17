package com.sunghyun.football.domain.noti.domain.repository;

import com.sunghyun.football.domain.noti.domain.FreeSubNoti;
import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;

import java.util.List;

public interface FreeSubNotiRepository {
    FreeSubNoti save(FreeSubNoti freeSubNoti);
    void saveAll(List<FreeSubNoti> list);
    List<FreeSubType> getFreeSubTypes(String email, Long matchNo);
    List<FreeSubNoti> getFreeSubNoties(Long memberNo,String nowDt);
    List<FreeSubNoti> getFreeSubNotiesByDate(Long memberNo,String selectedDt,Long notiNo,int pageSize);
    List<String> getNotiRegDtsByDt(Long memberNo,String startDt,String endDt);
    void delFreeSubNoti(Long notiNo);
}
