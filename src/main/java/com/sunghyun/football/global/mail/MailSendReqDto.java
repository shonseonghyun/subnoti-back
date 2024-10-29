package com.sunghyun.football.global.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MailSendReqDto {
    private String email;
    private String subject;
    private String content;
}
