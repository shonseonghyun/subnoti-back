package com.sunghyun.football.domain.noti.domain.repository;

import com.sunghyun.football.domain.noti.domain.MatchFreeSubNoti;

import java.util.Optional;

public interface FreeSubNotiRepository {

    MatchFreeSubNoti save(MatchFreeSubNoti freeSubNoti);
    Optional<MatchFreeSubNoti> getFreeSubNoti(String email,Long matchNo);
}
