package com.sunghyun.football.global.event.handler;

import com.sunghyun.football.global.event.event.NotificationSentEvent;
import com.sunghyun.football.global.noti.notification.NotificationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationSendEventHandler {
    private final NotificationFacade notificationFacade;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public <T> void sendNotification(final NotificationSentEvent<T> notificationSentEvent){
        notificationFacade.notify(
                notificationSentEvent.getNotiType(),
                notificationSentEvent.getEmail(),
                notificationSentEvent.getMessageDto()
                );
    }
}
