package com.sunghyun.football.domain.match.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MatchViewCount {
    private Long viewNo;

    private Integer viewCount;

    public void isClicked() {
        viewCount+=1;
    }
}
