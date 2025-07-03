package com.sunghyun.football.global.noti.message.build.dto;


import com.sunghyun.football.domain.noti.domain.enums.ActiveType;
import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreeSubNotiMessageDto {
    private Long matchNo;
    private String matchName;
    private String startDt;
    private String startTm;

    private FreeSubType freeSubType;
    private ActiveType activeType;
}
