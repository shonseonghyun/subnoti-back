package com.sunghyun.football.domain.noti.application.dto;

import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FreeSubNotiRegReqDto {
    @NotNull
    private Long memberNo;

    @NotNull
    private String email;


//    @Pattern(regexp = "^[0-9]$", message = "숫자만 입력할 수 있습니다.")
    @NotNull
    private Long matchNo;

    @NotNull
    private FreeSubType subType;
}
