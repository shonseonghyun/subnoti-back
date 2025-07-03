package com.sunghyun.football.global.noti.notification;

import com.sunghyun.football.global.noti.dto.NotiSendReqDto;

public interface NotificationService {
    void notify(NotiSendReqDto notiSendReqDto);
}
