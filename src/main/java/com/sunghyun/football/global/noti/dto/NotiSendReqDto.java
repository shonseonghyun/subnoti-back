package com.sunghyun.football.global.noti.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NotiSendReqDto {
    private String email;
    private String subject;
    private String content;
}
