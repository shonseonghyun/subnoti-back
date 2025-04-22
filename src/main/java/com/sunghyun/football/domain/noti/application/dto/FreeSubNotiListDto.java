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
public class FreeSubNotiListDto {
    private Long notiNo;

    private Long matchNo;

    private String matchName;

    private String startDt;

    private String startTm;

    private FreeSubType subType;

    public static FreeSubNotiListDto toDto(FreeSubNoti freeSubNoti) {
        FreeSubNotiListDto freeSubNotiListDto = new FreeSubNotiListDto();

        freeSubNotiListDto.notiNo=freeSubNoti.getNotiNo();
        freeSubNotiListDto.matchNo = freeSubNoti.getMatchNo();
        freeSubNotiListDto.matchName = freeSubNoti.getMatchName();
        freeSubNotiListDto.startDt = freeSubNoti.getStartDt();
        freeSubNotiListDto.startTm = freeSubNoti.getStartTm();
        freeSubNotiListDto.subType = freeSubNoti.getSubType();

        return freeSubNotiListDto;
    }
}
