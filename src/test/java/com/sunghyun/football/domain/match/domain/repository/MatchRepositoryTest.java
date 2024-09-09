package com.sunghyun.football.domain.match.domain.repository;

import com.sunghyun.football.domain.match.domain.dto.SearchMatchesReqDto;
import com.sunghyun.football.domain.match.domain.Match;
import com.sunghyun.football.domain.match.domain.enums.GenderRule;
import com.sunghyun.football.domain.match.domain.enums.MatchState;
import com.sunghyun.football.domain.match.domain.enums.MatchStatus;
import com.sunghyun.football.domain.match.domain.enums.PlayStatus;
import com.sunghyun.football.domain.match.infrastructure.SpringJpaMatchPlayerRepository;
import com.sunghyun.football.domain.match.infrastructure.SpringJpaMatchRepository;
import com.sunghyun.football.domain.match.infrastructure.entity.MatchEntity;
import com.sunghyun.football.domain.match.infrastructure.entity.MatchPlayerEntity;
import com.sunghyun.football.domain.member.domain.enums.MemberLevelType;
import com.sunghyun.football.repository.TestRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

//@DataJpaTest(includeFilters = @ComponentScan.Filter(Repository.class))
//@ActiveProfiles("dev")
//@Rollback(value = false)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MatchRepositoryTest extends TestRepository{
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private SpringJpaMatchRepository springJpaMatchRepository;


    @Autowired
    private SpringJpaMatchPlayerRepository springJpaMatchPlayerRepository;

    final Long stadiumNo=1L;
    final Long price = 10000L;
    final String startDt = "20240731";
    final String startTm = "0900";
    final Integer headCount =5;
    final MatchState matchState=MatchState.MATCH_AVAILABLE;

    final Long matchPlayer1 = 1L;
    final Long matchPlayer2 = 2L;

    @DisplayName("매치 등록 성공")
    @Test
    void regMatch(){
        //given
        MatchEntity matchEntity = createMatchEntityWithPlayer();
        //when
        MatchEntity savedMatchEntity = springJpaMatchRepository.save(matchEntity);

        //then
        Assertions.assertThat(savedMatchEntity.getStadiumNo()).isEqualTo(stadiumNo);
        Assertions.assertThat(savedMatchEntity.getHeadCount()).isEqualTo(headCount);
    }

    @Test
    @DisplayName("매치 참가 신청 성공")
    void applyMatch(){
        //given
        MatchEntity matchEntity =  createMatchEntityWithPlayer();
        MatchEntity savedMatchEntity = springJpaMatchRepository.save(matchEntity);

        //when
        savedMatchEntity.getPlayers().add(MatchPlayerEntity.builder().memberNo(matchPlayer1).playStatus(PlayStatus.MATCH_START_BEFORE).build());
        MatchEntity savedMatchEntityWithNewPlayer = springJpaMatchRepository.save(savedMatchEntity);

        //then
        Assertions.assertThat(savedMatchEntityWithNewPlayer.getPlayers().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("매치 참가 신청 취소 성공")
    void cancelMatch(){
        //given
        MatchEntity matchEntity = createMatchEntityWithPlayer();
        MatchEntity savedMatchEntity = springJpaMatchRepository.save(matchEntity);
        System.out.println("사이즈:"+savedMatchEntity.getPlayers().size());

        //when
        savedMatchEntity.getPlayers().remove(0);
        springJpaMatchRepository.save(savedMatchEntity);

        //then
        Assertions.assertThat(savedMatchEntity.getPlayers().size()).isEqualTo(0);
    }

    @DisplayName("매치 조회 성공")
    @Test
    void getMatch(){
        //given
        MatchEntity matchEntity = createMatchEntityWithPlayer();
        MatchEntity savedMatchEntity = springJpaMatchRepository.save(matchEntity);
        //when
        MatchEntity selectedMatchEntity = springJpaMatchRepository.findByMatchNo(savedMatchEntity.getMatchNo()).get();

        //then
        Assertions.assertThat(selectedMatchEntity.getMatchNo()).isEqualTo(savedMatchEntity.getMatchNo());
    }

    @DisplayName("매치 삭제 성공 - 신청한 사용자리스트도 함께 삭제")
    @Test
    void deleteMatch(){
        MatchEntity matchEntity = createMatchEntityWithPlayer();
        Match savedMatch = matchRepository.save(matchEntity.toModel());
        matchRepository.save(savedMatch);

        //when
        matchRepository.deleteMatch(savedMatch.getMatchNo());

        //then
        Optional<Match> selectedMatch = matchRepository.findByMatchNo(savedMatch.getMatchNo());
        Assertions.assertThat(selectedMatch).isNotPresent();
    }

    @DisplayName("특정 날짜 매치 전체 조회(사이즈0)")
    @Test
    void findAllMatchByStartDtOfSize0(){
        SearchMatchesReqDto searchMatchesReqDto = SearchMatchesReqDto.builder()
                .startDt(startDt)
                .build();
        List<Match> matches = matchRepository.findAllByConditions(searchMatchesReqDto);

        Assertions.assertThat(matches.size()).isEqualTo(0);
    }


    @DisplayName("특정 날짜 매치 전체 조회(사이즈2)")
    @Test
    void findAllMatchByStartDtOfSize2(){
        SearchMatchesReqDto searchMatchesReqDto = SearchMatchesReqDto.builder()
                .startDt(startDt)
                .build();

        MatchEntity matchEntity1 = createMatchEntityWithPlayer();
        MatchEntity matchEntity2 = createMatchEntityWithPlayer();
        matchRepository.save(matchEntity1.toModel());
        matchRepository.save(matchEntity2.toModel());

        List<Match> matches = matchRepository.findAllByConditions(searchMatchesReqDto);

        Assertions.assertThat(matches.size()).isEqualTo(2);
    }

    @DisplayName("존재하는 매치 전체 조회")
    @Test
    void findAllMatch(){
        MatchEntity matchEntity1 = createMatchEntityWithPlayer();
        MatchEntity matchEntity2 = createMatchEntityWithPlayer();
        matchRepository.save(matchEntity1.toModel());
        matchRepository.save(matchEntity2.toModel());

        List<Match> matches = matchRepository.findAll();

        Assertions.assertThat(matches.size()).isEqualTo(2);
    }

    private MatchEntity createMatchEntityWithOutPlayer(){
        return MatchEntity.builder()
                .stadiumNo(stadiumNo)
                .price(price)
                .startDt(startDt)
                .startTm(startTm)
                .headCount(headCount)
                .matchState(MatchState.MATCH_REG_MANAGER_BEFORE)
                .matchStatus(MatchStatus.MATCH_START_BEFORE)
                .build()
                ;
    }

    private MatchEntity createMatchEntityWithPlayer(){
        List<MatchPlayerEntity> players = new ArrayList<>();
        players.add(createMatchPlayerEntity());

        return MatchEntity.builder()
                .stadiumNo(stadiumNo)
                .price(price)
                .startDt(startDt)
                .startTm(startTm)
                .headCount(headCount)
                .levelRule(MemberLevelType.AMATEUR2)
                .genderRule(GenderRule.ALL)
                .matchState(MatchState.MATCH_REG_MANAGER_BEFORE)
                .matchStatus(MatchStatus.MATCH_START_BEFORE)
                .players(players)
                .build()
                ;
    }

    private MatchPlayerEntity createMatchPlayerEntity(){
        return MatchPlayerEntity.builder()
                .memberNo(matchPlayer2)
                .playStatus(PlayStatus.MATCH_START_BEFORE)
                .build()
                ;
    }
}