package com.sunghyun.football.domain.noti.application.dto;

import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FreeSubNotiReqDto {
    private String email;

//    @Pattern(regexp = "^[0-9]$", message = "숫자만 입력할 수 있습니다.")
    private Long matchNo;
//    private Long userNo;

    private FreeSubType subType;
}
