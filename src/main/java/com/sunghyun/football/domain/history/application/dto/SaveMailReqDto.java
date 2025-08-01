package com.sunghyun.football.domain.history.application.dto;

import com.sunghyun.football.global.noti.type.NotiType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveMailReqDto {
    @NotNull
    private String email;

    private String sendDt;

    private String sendTm;

    @NotNull
    private NotiType notiType;

    private String subject;

    private String content;
}