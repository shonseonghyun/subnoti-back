package com.sunghyun.football.domain.match.application.dto;

import com.sunghyun.football.domain.match.domain.Match;
import com.sunghyun.football.domain.stadium.application.dto.SelectStadiumResDto;
import com.sunghyun.football.domain.stadium.enums.EnumMapperValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SelectSimpleMatchResDto {
    private Long matchNo;
    private String stadiumName;
    private String startTm;
    public EnumMapperValue matchState;

    public static SelectSimpleMatchResDto from(Match match, SelectStadiumResDto selectStadiumResDto){
        SelectSimpleMatchResDto selectSimpleMatchResDto = new SelectSimpleMatchResDto();
        selectSimpleMatchResDto.matchNo = match.getMatchNo();
        selectSimpleMatchResDto.startTm = match.getStartTm();
        selectSimpleMatchResDto.stadiumName = selectStadiumResDto.getStadiumName();
        selectSimpleMatchResDto.matchState = new EnumMapperValue(match.getMatchState());
        return selectSimpleMatchResDto;
    }
}
