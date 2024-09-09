package com.sunghyun.football.domain.match.domain.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SearchMatchesReqDto {
    @Pattern(regexp = "^([12]\\d{3}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01]))$")
    String startDt;

    Long stadiumNo;
}
