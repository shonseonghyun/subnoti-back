package com.sunghyun.football.domain.match.application.dto;

import com.sunghyun.football.domain.match.domain.enums.GenderRule;
import com.sunghyun.football.domain.member.domain.enums.MemberLevelType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegMatchReqDto {
    @NotNull
    private Long stadiumNo;

    @NotNull
    private Long price;

//    @NotNull
//    private Long locationNo;

    @NotNull
    private String startDt;

    @NotNull
    private String startTm;

    @NotNull
    private Integer headCount;

    @NotNull
    private GenderRule genderRule;

    @NotNull
    private MemberLevelType levelRule;
}
