package com.sunghyun.football.domain.match.domain.enums;

import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.match.PlayStatusNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PlayStatus {
    MATCH_START_BEFORE(0,"매치 시작 전"),
    PLAY(1,"참가"),
    NOT_PLAY(2,"불참")
    ;

    private final Integer playStatusCode;
    private final String desc;

    public static PlayStatus getPlayStatus(Integer value){
        return Arrays.stream(PlayStatus.values())
                .filter(playStatus -> playStatus.getPlayStatusCode().equals(value))
                .findFirst()
                .orElseThrow(()->new PlayStatusNotFoundException(ErrorCode.PLAY_STATUS_NOT_FOUND));
    }

}
