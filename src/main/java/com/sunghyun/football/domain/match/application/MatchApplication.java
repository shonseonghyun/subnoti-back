package com.sunghyun.football.domain.match.application;

import com.sunghyun.football.domain.match.application.dto.RegMatchReqDto;
import com.sunghyun.football.domain.match.domain.dto.SearchMatchesReqDto;
import com.sunghyun.football.domain.match.application.dto.SelectMatchResDto;
import com.sunghyun.football.domain.match.application.dto.SelectSimpleMatchResDto;
import com.sunghyun.football.domain.match.domain.Match;
import com.sunghyun.football.domain.match.domain.MatchPlayer;
import com.sunghyun.football.domain.match.domain.enums.MatchState;
import com.sunghyun.football.domain.match.domain.enums.MatchStatus;
import com.sunghyun.football.domain.match.domain.repository.MatchRepository;
import com.sunghyun.football.domain.member.application.dto.SelectMemberResDto;
import com.sunghyun.football.domain.member.domain.enums.MemberLevelType;
import com.sunghyun.football.domain.stadium.application.dto.SelectStadiumResDto;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.match.MatchAlreadyRegSameTimeException;
import com.sunghyun.football.global.exception.exceptions.match.MatchApplyInSameTimeException;
import com.sunghyun.football.global.feign.MemberOpenFeignClient;
import com.sunghyun.football.global.feign.StadiumOpenFeignClient;
import com.sunghyun.football.global.response.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MatchApplication {
    private final MatchRepository matchRepository;
    private final MatchServiceHelper matchServiceHelper;

    private final StadiumOpenFeignClient stadiumOpenFeignClient;
    private final MemberOpenFeignClient memberOpenFeignClient;

    public void getMatches(){

    }

    public SelectMatchResDto getMatch(final Long matchNo){
        SelectMemberResDto selectedManagerResDto = null;
        SelectStadiumResDto selectedStadiumResDto =null;
        Map<Long,SelectMemberResDto> selectedMembersMap = new HashMap();

        Match selectedMatch = matchServiceHelper.findExistingMatch(matchNo);

        //스타디움 정보 구하기
        ApiResponseDto<SelectStadiumResDto> stadiumResponse = stadiumOpenFeignClient.checkExistStadium(selectedMatch.getStadiumNo());
        selectedStadiumResDto = stadiumResponse.getData();

        //매니저 정보 구하기
        if(selectedMatch.getManagerNo()!=null){
            ApiResponseDto<SelectMemberResDto> managerResponse = memberOpenFeignClient.checkExistMember(selectedMatch.getManagerNo());
            selectedManagerResDto= managerResponse.getData();
        }

        //멤버 정보(level,이름 등) 구하기
        List<MemberLevelType> memberLevelTypes = new ArrayList<>();

        for(MatchPlayer player:selectedMatch.getPlayers()){
            ApiResponseDto<SelectMemberResDto> memberResponse = memberOpenFeignClient.checkExistMember(player.getMemberNo());
            if(memberResponse==null){
                continue;
            }
            SelectMemberResDto memberResDto = memberResponse.getData();
            selectedMembersMap.put(memberResDto.getMemberNo(),memberResDto);
            MemberLevelType memberLevelType = memberResDto.getLevel();
            memberLevelTypes.add(memberLevelType);
        }
        selectedMatch.setAndCalAvgLevel(memberLevelTypes);

        return SelectMatchResDto.from(selectedMatch,selectedStadiumResDto,selectedManagerResDto,selectedMembersMap);
    }

    public void regMatch(final RegMatchReqDto regMatchReqDto) {
        stadiumOpenFeignClient.checkExistStadium(regMatchReqDto.getStadiumNo());

        final int count = matchRepository.findByStadiumNoAndPlayDt(regMatchReqDto.getStadiumNo(),regMatchReqDto.getStartDt(),regMatchReqDto.getStartTm());
        if(count>0){
            throw new MatchAlreadyRegSameTimeException(ErrorCode.MATCH_ALREADY_REQ_SAME_TIME);
        }

        Match match = Match.builder()
                .stadiumNo(regMatchReqDto.getStadiumNo())
                .headCount(regMatchReqDto.getHeadCount())
                .startDt(regMatchReqDto.getStartDt())
                .startTm(regMatchReqDto.getStartTm())
                .price(regMatchReqDto.getPrice())
                .players(Collections.EMPTY_LIST)
                .matchState(MatchState.MATCH_REG_MANAGER_BEFORE)
                .matchStatus(MatchStatus.MATCH_START_BEFORE)
                .levelRule(regMatchReqDto.getLevelRule())
                .genderRule(regMatchReqDto.getGenderRule())
                .build();

        matchRepository.save(match);
    }

    public void regManagerInMatch(final Long matchNo, final Long managerNo) {
        Match selectedMatch = matchServiceHelper.findExistingMatch(matchNo);

        stadiumOpenFeignClient.checkExistStadium(selectedMatch.getStadiumNo());
        memberOpenFeignClient.checkExistMember(managerNo);

        selectedMatch.regManager(managerNo);

        matchRepository.save(selectedMatch);
    }

    public void receivePlayer(final Long matchNo, final Long memberNo) {
        Match selectedMatch = matchServiceHelper.findExistingMatch(matchNo);

        final int count = matchRepository.findMatchPlayerByMemberNoAndPlayDt(memberNo,selectedMatch.getStartDt(),selectedMatch.getStartTm());
        if(count>0){
            throw new MatchApplyInSameTimeException(ErrorCode.MATCH_ALREADY_APPLY_SAME_TIME);
        }

        stadiumOpenFeignClient.checkExistStadium(selectedMatch.getStadiumNo());
        ApiResponseDto<SelectMemberResDto> response= memberOpenFeignClient.checkExistMember(memberNo);
        SelectMemberResDto selectedMemberResDto=response.getData();

        selectedMatch.receivePlayer(memberNo,selectedMemberResDto.getLevel(),selectedMemberResDto.getGender());

        matchRepository.save(selectedMatch);
    }

    public void cancelPlayer(Long matchNo, Long memberNo) {
        Match selectedMatch = matchServiceHelper.findExistingMatch(matchNo);

        stadiumOpenFeignClient.checkExistStadium(selectedMatch.getStadiumNo());
        memberOpenFeignClient.checkExistMember(memberNo);

        selectedMatch.cancelPlayer(memberNo);

        matchRepository.deletePlayer(selectedMatch,memberNo);
    }

    public List<SelectSimpleMatchResDto> getMatchesByConditions(SearchMatchesReqDto searchMatchesReqDto) {
        final List<SelectSimpleMatchResDto> result = new ArrayList<>();
        final List<Match> matches = matchRepository.findAllByConditions(searchMatchesReqDto);
        for(Match match:matches){
            ApiResponseDto<SelectStadiumResDto> stadiumResponse = stadiumOpenFeignClient.checkExistStadium(match.getStadiumNo());
            SelectStadiumResDto selectedStadiumResDto = stadiumResponse.getData();
            result.add(SelectSimpleMatchResDto.from(match,selectedStadiumResDto));
        }
        return result;
    }
}
