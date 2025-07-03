package com.sunghyun.football.global.noti.notification.kakao;

import com.sunghyun.football.global.noti.dto.NotiSendReqDto;
import com.sunghyun.football.global.noti.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KaKaoTalkNotiService implements NotificationService {


    @Async
    @Override
    public void notify(NotiSendReqDto notiSendReqDto) {
        log.info("[{}] 카카오톡 알림 발송",notiSendReqDto.getSubject());
    }
}
