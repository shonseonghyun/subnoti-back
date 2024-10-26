package com.sunghyun.football.domain.noti.domain.repository;

import com.sunghyun.football.domain.noti.domain.MatchFreeSubNoti;
import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;

import java.util.List;

public interface FreeSubNotiRepository {

    MatchFreeSubNoti save(MatchFreeSubNoti freeSubNoti);
    List<FreeSubType> getFreeSubTypes(String email, Long matchNo);
}
