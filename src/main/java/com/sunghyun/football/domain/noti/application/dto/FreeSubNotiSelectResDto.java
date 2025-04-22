package com.sunghyun.football.domain.noti.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FreeSubNotiSelectResDto {
    Long nextNotiNo;
    List<FreeSubNotiListDto> freeSubNotiList;
}
