package com.sunghyun.football.domain.match.infrastructure.entity;

import com.sunghyun.football.domain.match.domain.MatchViewCount;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "match_view")
@Entity
public class MatchViewCountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewNo;

    private Integer viewCount;

    public static MatchViewCountEntity from(MatchViewCount matchViewCount){
        MatchViewCountEntity matchViewCountEntity = new MatchViewCountEntity();
        matchViewCountEntity.viewNo = matchViewCount.getViewNo();
        matchViewCountEntity.viewCount = matchViewCount.getViewCount();
        return matchViewCountEntity;
    }

    public MatchViewCount toModel() {
        return MatchViewCount.builder()
                .viewNo(viewNo)
                .viewCount(viewCount)
                .build()
                ;
    }


}
