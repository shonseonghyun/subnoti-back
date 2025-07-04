//package com.sunghyun.football.domain.match.infrastructure.entity;
//
//import com.sunghyun.football.domain.match.domain.Match;
//import com.sunghyun.football.domain.match.domain.enums.GenderRule;
//import com.sunghyun.football.domain.match.domain.enums.MatchState;
//import com.sunghyun.football.domain.match.domain.enums.MatchStatus;
//import com.sunghyun.football.domain.match.infrastructure.entity.converter.MatchStateConverter;
//import com.sunghyun.football.domain.match.infrastructure.entity.converter.MatchStatusConverter;
//import com.sunghyun.football.domain.match.infrastructure.entity.converter.MemberLevelTypeConverter;
//import com.sunghyun.football.domain.member.domain.enums.MemberLevelType;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Getter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "matching")
//public class MatchEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long matchNo;
//
//    private Long stadiumNo;
//
//    private Long price;
//
//    private Long managerNo;
//
//    private String startDt;
//
//    private String startTm;
//
//    private Integer headCount;
//
//    @Enumerated(EnumType.STRING)
//    private GenderRule genderRule;
//
//    @Convert(converter = MemberLevelTypeConverter.class)
//    private MemberLevelType levelRule;
//
//    @Convert(converter = MatchStateConverter.class)
//    private MatchState matchState;
//
//    @OneToMany(cascade = CascadeType.ALL
////            {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH}
//            ,orphanRemoval = true) //EAGER 개선 필요
//    @JoinColumn(name = "match_no")
//    private List<MatchPlayerEntity> players;
//
//    @Convert(converter = MatchStatusConverter.class)
//    private MatchStatus matchStatus;
//
////    @OneToOne(mappedBy = "match", cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.LAZY)
////    private MatchViewCountEntity viewCount;
//
//    private Integer viewCount;
//
//    @Version
//    private Long version;
//
//    public static MatchEntity from(Match match){
//        MatchEntity matchEntity=new MatchEntity();
//        matchEntity.matchNo=match.getMatchNo();
//        matchEntity.stadiumNo=match.getStadiumNo();
//        matchEntity.price=match.getPrice();
//        matchEntity.managerNo=match.getManagerNo();
//        matchEntity.startDt=match.getStartDt();
//        matchEntity.startTm=match.getStartTm();
//        matchEntity.headCount=match.getHeadCount();
//        matchEntity.levelRule = match.getLevelRule();
//        matchEntity.genderRule = match.getGenderRule();
//        matchEntity.matchState=match.getMatchState();
//        matchEntity.players=match.getPlayers().stream().map(MatchPlayerEntity::from).collect(Collectors.toList());
//        matchEntity.matchStatus=match.getMatchStatus();
//        matchEntity.genderRule=match.getGenderRule();
//        matchEntity.levelRule=match.getLevelRule();
////        matchEntity.viewCount = MatchViewCountEntity.from(match.getViewCount(),matchEntity);
//        matchEntity.viewCount = match.getViewCount();
//        matchEntity.version = match.getVersion();
//        return matchEntity;
//    }
//
//    public Match toModel(){
//        return Match.builder()
//                .matchNo(matchNo)
//                .stadiumNo(stadiumNo)
//                .price(price)
//                .managerNo(managerNo)
//                .startDt(startDt)
//                .startTm(startTm)
//                .headCount(headCount)
//                .genderRule(genderRule)
//                .levelRule(levelRule)
//                .matchState(matchState)
//                .players(players.stream().map(MatchPlayerEntity::toModel).collect(Collectors.toList()))
//                .matchStatus(matchStatus)
//                .viewCount(viewCount)
//                .version(version)
//                .build()
//                ;
//    }
//
//}
