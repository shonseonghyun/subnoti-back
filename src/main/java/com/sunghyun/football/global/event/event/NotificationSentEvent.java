package com.sunghyun.football.global.event.event;

import com.sunghyun.football.global.noti.type.NotiType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class NotificationSentEvent<T> {
    private NotiType notiType;
    private String email;
    private T messageDto;

}
