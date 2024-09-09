package com.sunghyun.football.domain.match.application.dto;

import com.sunghyun.football.domain.match.domain.MatchPlayer;
import com.sunghyun.football.domain.match.domain.enums.PlayStatus;
import com.sunghyun.football.domain.member.application.dto.SelectMemberResDto;
import com.sunghyun.football.domain.member.domain.enums.MemberLevelType;
import lombok.Getter;

@Getter
public class MatchPlayerDto {
    private Long playerNo;

    private Long memberNo;

    private String name;

    private PlayStatus playStatus;

    private MemberLevelType level;

    public static MatchPlayerDto from(MatchPlayer matchPlayer) {
        MatchPlayerDto matchPlayerDto = new MatchPlayerDto();
        matchPlayerDto.playerNo = matchPlayer.getPlayerNo();
        matchPlayerDto.memberNo = matchPlayer.getMemberNo();
        matchPlayerDto.playStatus = matchPlayer.getPlayStatus();
        return  matchPlayerDto;
    }

    public static MatchPlayerDto from(MatchPlayer matchPlayer, SelectMemberResDto selectMemberResDto) {
        MatchPlayerDto matchPlayerDto = new MatchPlayerDto();
        matchPlayerDto.playerNo = matchPlayer.getPlayerNo();
        matchPlayerDto.memberNo = matchPlayer.getMemberNo();
        matchPlayerDto.playStatus = matchPlayer.getPlayStatus();

        matchPlayerDto.name = selectMemberResDto.getName();
        matchPlayerDto.level = selectMemberResDto.getLevel();
        return  matchPlayerDto;
    }
}
