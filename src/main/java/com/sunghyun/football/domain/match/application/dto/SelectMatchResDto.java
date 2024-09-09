package com.sunghyun.football.domain.match.application.dto;

import com.sunghyun.football.domain.match.domain.Match;
import com.sunghyun.football.domain.member.application.dto.SelectMemberResDto;
import com.sunghyun.football.domain.stadium.application.dto.SelectStadiumResDto;
import com.sunghyun.football.domain.stadium.enums.EnumMapperValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
    public class SelectMatchResDto {

    private Long matchNo;

    private Long price;

    private SelectStadiumResDto stadium;

    private ManagerDto manager;

    private String startDt;

    private String startTm;

    private Integer headCount;

    private EnumMapperValue matchState;

    private List<MatchPlayerDto> players;

    private List<EnumMapperValue> levels;

    private EnumMapperValue avgMatchLevel;

    private EnumMapperValue matchStatus;

    private EnumMapperValue genderRule;

    private EnumMapperValue levelRule;

    public static SelectMatchResDto from(Match match, SelectStadiumResDto stadiumResDto) {
        SelectMatchResDto selectMatchResDto = new SelectMatchResDto();
        selectMatchResDto.matchNo = match.getMatchNo();
        selectMatchResDto.stadium = stadiumResDto;
        selectMatchResDto.price = match.getPrice();
        selectMatchResDto.startDt = match.getStartDt();
        selectMatchResDto.startTm = match.getStartTm();
        selectMatchResDto.headCount = match.getHeadCount();
        selectMatchResDto.matchState = new EnumMapperValue(match.getMatchState());
        selectMatchResDto.matchStatus = new EnumMapperValue(match.getMatchStatus());
        selectMatchResDto.levels = match.getLevels().stream()
                                    .map(EnumMapperValue::new)
                                    .collect(Collectors.toList());
        selectMatchResDto.levelRule = new EnumMapperValue(match.getLevelRule());
        selectMatchResDto.genderRule = new EnumMapperValue(match.getGenderRule());

        return selectMatchResDto;
    }

    public static SelectMatchResDto from(Match match, SelectStadiumResDto stadiumResDto, SelectMemberResDto managerResDto, Map<Long,SelectMemberResDto> memberMap) {
        SelectMatchResDto selectMatchResDto = from(match,stadiumResDto);

        //매니저
        if(managerResDto!=null){
            selectMatchResDto.manager = new ManagerDto();
            selectMatchResDto.getManager().setMemberNo(managerResDto.getMemberNo());
            selectMatchResDto.getManager().setManagerName(managerResDto.getName());

            selectMatchResDto.players = match.getPlayers().stream()
                    .map(matchPlayer->MatchPlayerDto.from(matchPlayer,memberMap.get(matchPlayer.getMemberNo())))
                    .collect(Collectors.toList());
        }

        //매치 레벨(레벨들,평균레벨)
        selectMatchResDto.avgMatchLevel=new EnumMapperValue(match.getAvgMatchLevel());
        return selectMatchResDto;
    }


}
