package com.sunghyun.football.domain.noti.application.dto;

import com.sunghyun.football.domain.noti.domain.FreeSubNoti;
import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectFreeSubNotiResDto {
    private Long notiNo;

    private Long matchNo;

    private String matchName;

    private String startDt;

    private String startTm;

    private FreeSubType subType;

    public static SelectFreeSubNotiResDto toDto(FreeSubNoti freeSubNoti) {
        SelectFreeSubNotiResDto selectFreeSubNotiResDto = new SelectFreeSubNotiResDto();

        selectFreeSubNotiResDto.notiNo=freeSubNoti.getNotiNo();
        selectFreeSubNotiResDto.matchNo = freeSubNoti.getMatchNo();
        selectFreeSubNotiResDto.matchName = freeSubNoti.getMatchName();
        selectFreeSubNotiResDto.startDt = freeSubNoti.getStartDt();
        selectFreeSubNotiResDto.startTm = freeSubNoti.getStartTm();
        selectFreeSubNotiResDto.subType = freeSubNoti.getSubType();

        return selectFreeSubNotiResDto;
    }
}
