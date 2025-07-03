package com.sunghyun.football.global.noti.message.build;

import com.sunghyun.football.global.noti.type.NotiType;

public interface MessageBuilder<T> {
    String buildSubject(T dto);
    String buildContent(T dto);
    NotiType getNotiType();
}
