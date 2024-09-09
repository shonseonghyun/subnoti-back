package com.sunghyun.football.domain.match.domain.enums;

import com.sunghyun.football.domain.stadium.enums.EnumMapperType;
import com.sunghyun.football.domain.stadium.enums.EnumMapperValue;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.match.MatchStateNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum MatchStatus implements EnumMapperType {
    MATCH_START_BEFORE(0,"매치 시작 전"),
    MATCH_END_NORMAL(1,"매치 정상 종료"),
    MATCH_END_ABNORMAL(2,"매치 비정상 종료" );

    private final Integer matchStatusCode;
    private final String desc;

    public static MatchStatus getMatchStatus(Integer value){
        return Arrays.stream(MatchStatus.values())
                .filter(matchStatus -> matchStatus.getMatchStatusCode().equals(value))
                .findFirst()
                .orElseThrow(()->new MatchStateNotFoundException(ErrorCode.MATCH_STATE_NOT_FOUND));
    }

    @Override
    public String getName() {
        return name();
    }
}
