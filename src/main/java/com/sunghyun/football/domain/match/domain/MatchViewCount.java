package com.sunghyun.football.domain.match.domain;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MatchViewCount {
    private Long viewNo;

    private Integer viewCount;

//    private Long version;

    public void isClicked() {
        viewCount+=1;
    }
}
