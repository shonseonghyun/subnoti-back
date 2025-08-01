package com.sunghyun.football.global.noti.notification;

import com.sunghyun.football.domain.history.application.dto.SaveMailReqDto;
import com.sunghyun.football.domain.history.application.service.MailHistoryService;
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
        private final MailHistoryService mailHistoryService;

        public <T> void notify(final NotiType notiType,final String email,final T messageDto){
            String subject = messageBuilderFactory.buildSubject(notiType,messageDto);
            String content = messageBuilderFactory.buildContent(notiType,messageDto);
            notificationService.notify(new NotiSendReqDto(email,subject,content));


            //노티 발송 시 노티 발송 이력까지 함께 저장해야 한다.
            mailHistoryService.saveMailHistory(new SaveMailReqDto(
                email,
                    "날짜",
                    "시간",
                    notiType,
                    subject,
                    content
            ));
    }

}
