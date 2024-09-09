package com.sunghyun.football.domain.match.domain;

import com.sunghyun.football.domain.match.domain.enums.GenderRule;
import com.sunghyun.football.domain.match.domain.enums.MatchState;
import com.sunghyun.football.domain.member.domain.enums.Gender;
import com.sunghyun.football.domain.member.domain.enums.MemberLevelType;
import com.sunghyun.football.global.exception.exceptions.match.GenderRuleException;
import com.sunghyun.football.global.exception.exceptions.match.LevelRuleException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;


class MatchTest {

    @DisplayName("매치 평균 레벨 구하기")
    @Test
    void getAverageLevel(){
        Match match = Match.builder()
                .build()
                ;
        match.setAndCalAvgLevel(Arrays.asList(MemberLevelType.ROOKIE, MemberLevelType.ROOKIE, MemberLevelType.AMATEUR1, MemberLevelType.AMATEUR2));

        Assertions.assertThat(match.getAvgMatchLevel()).isEqualTo(MemberLevelType.AMATEUR2);
    }

    @DisplayName("매칭 레벨 제한있는 경우 해당 제한에 걸린 케이스")
    @Test
    void applyMatchWithLevelRule(){
        Match match = Match.builder()
                .managerNo(1L)
                .players(new ArrayList<>())
                .matchState(MatchState.MATCH_AVAILABLE)
                .levelRule(MemberLevelType.AMATEUR2)
                .genderRule(GenderRule.ALL)
                .build();
        Assertions.assertThatThrownBy(()->match.receivePlayer(1L, MemberLevelType.AMATEUR3, Gender.FEMALE))
                .isInstanceOf(LevelRuleException.class)
        ;
    }

    @DisplayName("매칭 성별 제한있는 경우 해당 제한에 걸린 케이스")
    @Test
    void applyMatchWithGenderRule(){
        Match match = Match.builder()
                .managerNo(1L)
                .players(new ArrayList<>())
                .matchState(MatchState.MATCH_AVAILABLE)
                .levelRule(MemberLevelType.PRO3)
                .genderRule(GenderRule.FEMALE)
                .build();
        Assertions.assertThatThrownBy(()->match.receivePlayer(1L, MemberLevelType.AMATEUR3, Gender.MALE))
                .isInstanceOf(GenderRuleException.class)
        ;
    }
}