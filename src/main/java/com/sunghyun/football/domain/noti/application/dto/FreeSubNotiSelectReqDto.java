package com.sunghyun.football.domain.noti.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FreeSubNotiSelectReqDto {
    @NotNull
    private Long memberNo;
}
