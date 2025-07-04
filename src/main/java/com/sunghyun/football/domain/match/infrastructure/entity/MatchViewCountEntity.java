//package com.sunghyun.football.domain.match.infrastructure.entity;
//
//import com.sunghyun.football.domain.match.domain.MatchViewCount;
//import jakarta.persistence.*;
//import lombok.Getter;
//
//@Getter
//@Table(name = "match_view")
//@Entity
//public class MatchViewCountEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long viewNo;
//
//    private Integer viewCount;
//
//    @OneToOne
//    @JoinColumn(name = "match_no")
//    private MatchEntity match;
//
////    @Version //Optimistic Lock을 위한 version 컬럼
////    private Long version;
//
//    public static MatchViewCountEntity from(MatchViewCount matchViewCount,MatchEntity matchEntity){
//        MatchViewCountEntity matchViewCountEntity = new MatchViewCountEntity();
//        matchViewCountEntity.viewNo = matchViewCount.getViewNo();
//        matchViewCountEntity.viewCount = matchViewCount.getViewCount();
////        matchViewCountEntity.version = matchViewCount.getVersion();
//        matchViewCountEntity.match = matchEntity;
//        return matchViewCountEntity;
//    }
//
//    public MatchViewCount toModel() {
//        return MatchViewCount.builder()
//                .viewNo(viewNo)
//                .viewCount(viewCount)
////                .version(version)
//                .build()
//                ;
//    }
//
//
//}
