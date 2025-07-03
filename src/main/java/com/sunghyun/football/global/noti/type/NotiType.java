package com.sunghyun.football.global.noti.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NotiType {
    NOTI_JOIN("회원가입"),
    NOTI_REG("노티 등록"),
    NOTI_FREE_SUB("서브 알림")
    ;
    private final String desc;


}
