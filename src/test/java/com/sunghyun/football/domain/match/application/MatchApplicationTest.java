package com.sunghyun.football.domain.match.application;

import com.sunghyun.football.domain.match.application.dto.RegMatchReqDto;
import com.sunghyun.football.domain.match.domain.dto.SearchMatchesReqDto;
import com.sunghyun.football.domain.match.application.dto.SelectMatchResDto;
import com.sunghyun.football.domain.match.application.dto.SelectSimpleMatchResDto;
import com.sunghyun.football.domain.match.domain.Match;
import com.sunghyun.football.domain.match.domain.MatchPlayer;
import com.sunghyun.football.domain.match.domain.enums.GenderRule;
import com.sunghyun.football.domain.match.domain.enums.MatchState;
import com.sunghyun.football.domain.match.domain.enums.MatchStatus;
import com.sunghyun.football.domain.match.domain.repository.MatchRepository;
import com.sunghyun.football.domain.member.application.dto.SelectMemberResDto;
import com.sunghyun.football.domain.member.domain.enums.Gender;
import com.sunghyun.football.domain.member.domain.enums.MemberLevelType;
import com.sunghyun.football.domain.stadium.application.dto.SelectStadiumResDto;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.match.MatchAlreadyRegSameTimeException;
import com.sunghyun.football.global.exception.exceptions.match.MatchPlayerExistException;
import com.sunghyun.football.global.exception.exceptions.match.MatchPlayerNotFoundException;
import com.sunghyun.football.global.feign.MemberOpenFeignClient;
import com.sunghyun.football.global.feign.StadiumOpenFeignClient;
import com.sunghyun.football.global.response.ApiResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchApplicationTest {

    @InjectMocks
    private MatchApplication target;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private StadiumOpenFeignClient stadiumOpenFeignClient;

    @Mock
    private MemberOpenFeignClient memberOpenFeignClient;

    @Mock
    private MatchServiceHelper matchServiceHelper;


    final Long stadiumNo=1L;
    final Long price = 10000L;
    final String startDt = "20240731";
    final String startTm = "0900";
    final Integer headCount =5;
    final Integer min = (headCount*2) + 2;
    final Integer max = headCount*3;
    final Long managerNo = 100L;

    final Long memberNo = 10L;
    final Long anotherMemberNo = 22L;

    @DisplayName("매치 생성 시 이미 존재하는 시간대의 매치인 경우 익셉션")
    @Test
    void regMatchWithSameTimeMatch(){
        //given
        RegMatchReqDto regMatchReqDto = createRegMatchDto();
        doReturn(1).when(matchRepository).findByStadiumNoAndPlayDt(stadiumNo,startDt,startTm);

        //when,then
        Assertions.assertThatThrownBy(()->target.regMatch(regMatchReqDto))
                .isInstanceOf(MatchAlreadyRegSameTimeException.class)
                ;
    }

    @DisplayName("매치 생성")
    @Test
    void regMatch(){
        //given
        RegMatchReqDto regMatchReqDto = createRegMatchDto();

        //when
        target.regMatch(regMatchReqDto);

        //then
        verify(matchRepository,times(1)).save(any(Match.class));
    }


    private RegMatchReqDto createRegMatchDto(){
        return RegMatchReqDto.builder()
                .stadiumNo(stadiumNo).price(price)
                .startDt(startDt).startTm(startTm)
                .headCount(headCount)
                .build();
    }

    @DisplayName("특정 매치 매니저 임명")
    @Test
    void regManagerInMatch(){
        //given
        final Long matchNo = 2L;
        final Long managerNo = 10L;
        Match match= createMatchWithOutManager();
        //when
        doReturn(match).when(matchServiceHelper).findExistingMatch(matchNo);
        target.regManagerInMatch(matchNo,managerNo);

        //then
        verify(matchRepository,times(1)).save(any(Match.class));
        Assertions.assertThat(match.getMatchState()).isEqualTo(MatchState.MATCH_AVAILABLE);
    }

    private Match createMatchWithOutManager(){
                return Match.builder()
                        .stadiumNo(stadiumNo)
                        .price(price)
                        .startDt(startDt)
                        .startTm(startTm)
                        .headCount(headCount)
                        .players(Arrays.asList(createPlayer(memberNo)))
                        .matchState(MatchState.MATCH_REG_MANAGER_BEFORE)
                        .matchStatus(MatchStatus.MATCH_END_NORMAL)
                        .build();
    }

    @DisplayName("특정 매치 조회")
    @Test
    void getMatch(){
        //given
        final Long matchNo = 2L;
        Match match = createMatch();
        doReturn(match).when(matchServiceHelper).findExistingMatch(matchNo);
        doReturn(ApiResponseDto.toResponse(ErrorCode.SUCCESS,SelectStadiumResDto.builder().stadiumNo(10L).build())).when(stadiumOpenFeignClient).checkExistStadium(match.getStadiumNo());

        //when
        SelectMatchResDto selectMatchResDto = target.getMatch(matchNo);

        //then
        verify(matchServiceHelper,times(1)).findExistingMatch(matchNo);
        Assertions.assertThat(selectMatchResDto.getHeadCount()).isEqualTo(headCount);
    }


    @DisplayName("매치 신청 실패_이미 존재하는 회원이 신청한 경우")
    @Test
    void applyMatchWithAlreadyApplyedMember(){
        //given
        final Long matchNo = 2L;
        Match match = createMatchWithPlayerAndManager(memberNo);
        doReturn(match).when(matchServiceHelper).findExistingMatch(matchNo);
        doReturn(ApiResponseDto.toResponse(ErrorCode.SUCCESS,SelectMemberResDto.builder().build())).when(memberOpenFeignClient).checkExistMember(memberNo);

        //when,then
        Assertions.assertThatThrownBy(()->target.receivePlayer(matchNo,memberNo))
                .isInstanceOf(MatchPlayerExistException.class);
    }

    private Match createMatchWithPlayerAndManager(Long memberNo){
        return  Match.builder()
                .stadiumNo(stadiumNo)
                .price(price)
                .startDt(startDt)
                .startTm(startTm)
                .managerNo(managerNo)
                .matchStatus(MatchStatus.MATCH_START_BEFORE)
                .matchState(MatchState.MATCH_AVAILABLE)
                .headCount(headCount)
                .players(Arrays.asList(createPlayer(memberNo)))
                .build();
    }

    @DisplayName("최소 인원 신청 완료 시 matchState 변경 확인")
    @Test
    void applyMatchWithMin(){
        //given
        final Long matchNo = 2L;
        Match match = createMatchWithPlayerAndManager(memberNo,min);
        doReturn(match).when(matchServiceHelper).findExistingMatch(matchNo);
        doReturn(ApiResponseDto.toResponse(ErrorCode.SUCCESS,SelectMemberResDto.builder().level(MemberLevelType.AMATEUR1).gender(Gender.FEMALE).build())).when(memberOpenFeignClient).checkExistMember(anotherMemberNo);

        //when
        target.receivePlayer(matchNo,anotherMemberNo);

        //then
        Assertions.assertThat(match.getMatchState()).isEqualTo(MatchState.MATCH_ALMOST_DONE);
        Assertions.assertThat(match.getPlayers().size()).isGreaterThan(min);
    }

    private Match createMatchWithPlayerAndManager(Long memberNo,int count){
        List<MatchPlayer> players = new ArrayList<>();
        for(int i=0;i<count;i++){
            if(i==0){
                players.add(createPlayer(memberNo));
                continue;
            }
            players.add(createPlayer());
        }
        return  Match.builder()
                .stadiumNo(stadiumNo)
                .price(price)
                .startDt(startDt)
                .startTm(startTm)
                .managerNo(managerNo)
                .levelRule(MemberLevelType.AMATEUR2)
                .genderRule(GenderRule.FEMALE)
                .matchStatus(MatchStatus.MATCH_START_BEFORE)
                .matchState(MatchState.MATCH_AVAILABLE)
                .headCount(headCount)
                .players(players)
                .build();
    }

    @DisplayName("최대 인원 신청 완료 시 matchState 변경 확인")
    @Test
    void applyMatchWithMax(){
        //given
        final Long matchNo = 2L;
        Match match = createMatchWithPlayerAndManager(memberNo,max-1);
        doReturn(match).when(matchServiceHelper).findExistingMatch(matchNo);
        doReturn(ApiResponseDto.toResponse(ErrorCode.SUCCESS,SelectMemberResDto.builder().level(MemberLevelType.AMATEUR1).gender(Gender.FEMALE).build())).when(memberOpenFeignClient).checkExistMember(anotherMemberNo);

        //when
        target.receivePlayer(matchNo,anotherMemberNo);

        //then
        Assertions.assertThat(match.getMatchState()).isEqualTo(MatchState.MATCH_END);
        Assertions.assertThat(match.getPlayers().size()).isEqualTo(max);
    }

    @DisplayName("매치 신청 취소 실패_매치 내 존재하지 않는 회원이 취소 신청한 경우")
    @Test
    void cancelMatchWithNotExistMember(){
        //given
        final Long matchNo = 2L;
        Match match = createMatchWithPlayerAndManager(memberNo);
        doReturn(match).when(matchServiceHelper).findExistingMatch(matchNo);

        //when,then
        Assertions.assertThatThrownBy(()->target.cancelPlayer(matchNo,anotherMemberNo))
                .isInstanceOf(MatchPlayerNotFoundException.class);
    }

    @DisplayName("최대 인원 신청완료 시 신청 취소 요청 인입 시 matchState 변경 확인")
    @Test
    void cancelMatchWithMax(){
        //given
        final Long matchNo = 2L;
        final Long memberNo = 10L;
        Match match = createMatchWithPlayerAndManager(memberNo,max);
        doReturn(match).when(matchServiceHelper).findExistingMatch(matchNo);

        //when
        target.cancelPlayer(matchNo,memberNo);

        //then
        Assertions.assertThat(match.getMatchState()).isEqualTo(MatchState.MATCH_ALMOST_DONE);
        Assertions.assertThat(match.getPlayers().size()).isEqualTo(max-1);
    }

    @DisplayName("최소 인원 신청완료 시 신청 취소 요청 인입 시 matchState 변경 확인")
    @Test
    void cancelMatchWithMin(){
        //given
        final Long matchNo = 2L;
        Match match = createMatchWithPlayerAndManager(memberNo,min+1);
        doReturn(match).when(matchServiceHelper).findExistingMatch(matchNo);

        //when
        target.cancelPlayer(matchNo,memberNo);

        //then
        Assertions.assertThat(match.getMatchState()).isEqualTo(MatchState.MATCH_AVAILABLE);
        Assertions.assertThat(match.getPlayers().size()).isEqualTo(min);
    }

    @DisplayName("매치 특정 일차 전체 조회")
    @Test
    void getAllMatch(){
        SearchMatchesReqDto searchMatchesReqDto = SearchMatchesReqDto.builder()
                        .startDt(startDt)
                    .stadiumNo(stadiumNo)
                                .build();
        doReturn(any(ApiResponseDto.class))
//                .toResponse(ErrorCode.SUCCESS,SelectStadiumResDto.builder().build()))
                .when(stadiumOpenFeignClient).checkExistStadium(1L);

        doReturn(Arrays.asList(
                Match.builder().build(),
                Match.builder().build(),
                Match.builder().build()
        )).when(matchRepository).findAllByConditions(searchMatchesReqDto);


        doReturn(any(ApiResponseDto.class)).when(stadiumOpenFeignClient).checkExistStadium(stadiumNo);
//        doReturn(ApiResponseDto.toResponse(ErrorCode.SUCCESS,SelectStadiumResDto.builder().build())).when(stadiumOpenFeignClient).checkExistStadium(stadiumNo);
        final List<SelectSimpleMatchResDto> result = target.getMatchesByConditions(searchMatchesReqDto);

        Assertions.assertThat(result.size()).isEqualTo(3);
    }

    @DisplayName("매칭 레벨 제한있는 경우 해당 제한에 걸린 케이스")
    @Test
    void applyMatchWithLevelRule(){
        Match match = createMatchWithLevelRule(MemberLevelType.AMATEUR2);
    }

    private Match createMatch(){
        return  Match.builder()
                .stadiumNo(stadiumNo)
                .price(price)
                .startDt(startDt)
                .startTm(startTm)
                .headCount(headCount)
                .players(Arrays.asList(createPlayer(memberNo)))
                .levelRule(MemberLevelType.AMATEUR2)
                .genderRule(GenderRule.FEMALE)
                .matchState(MatchState.MATCH_REG_MANAGER_BEFORE)
                .matchStatus(MatchStatus.MATCH_START_BEFORE)
                .build();
    }

    private Match createMatchWithLevelRule(MemberLevelType level){
        return  Match.builder()
                .stadiumNo(stadiumNo)
                .price(price)
                .startDt(startDt)
                .startTm(startTm)
                .headCount(headCount)
                .players(Arrays.asList(createPlayer(memberNo)))
                .levelRule(MemberLevelType.AMATEUR2)
                .build();
    }

    private MatchPlayer createPlayer(){
        return MatchPlayer.builder().build();
    }

    private MatchPlayer createPlayer(Long memberNo){
        return MatchPlayer.builder().memberNo(memberNo).build();
    }
}