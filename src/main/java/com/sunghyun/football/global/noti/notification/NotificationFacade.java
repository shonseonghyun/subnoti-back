package com.sunghyun.football.global.noti.notification;

import com.sunghyun.football.global.noti.dto.NotiSendReqDto;
import com.sunghyun.football.global.noti.message.build.MessageBuilderFactory;
import com.sunghyun.football.global.noti.type.NotiType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationFacade {

        private final NotificationService notificationService;
        private final MessageBuilderFactory messageBuilderFactory;

        public <T> void notify(final NotiType notiType,final String email,final T messageDto){
            String subject = messageBuilderFactory.buildSubject(notiType,messageDto);
            String content = messageBuilderFactory.buildContent(notiType,messageDto);
        notificationService.notify(new NotiSendReqDto(email,subject,content));
    }

}
