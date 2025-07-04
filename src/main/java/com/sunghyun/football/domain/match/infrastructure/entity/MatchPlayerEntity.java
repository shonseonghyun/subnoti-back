//package com.sunghyun.football.domain.match.infrastructure.entity;
//
//import com.sunghyun.football.domain.match.domain.MatchPlayer;
//import com.sunghyun.football.domain.match.domain.enums.PlayStatus;
//import com.sunghyun.football.domain.match.infrastructure.entity.converter.PlayStatusConverter;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.NoArgsConstructor;
//
//
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name="match_player")
//public class MatchPlayerEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long playerNo;
//
//    private Long memberNo;
//
//    @Convert(converter = PlayStatusConverter.class)
//    private PlayStatus playStatus;
//
//    public static MatchPlayerEntity from(MatchPlayer matchPlayer){
//        MatchPlayerEntity matchPlayerEntity= new MatchPlayerEntity();
//        matchPlayerEntity.playerNo =matchPlayer.getPlayerNo();
//        matchPlayerEntity.memberNo=matchPlayer.getMemberNo();
//        matchPlayerEntity.playStatus=matchPlayer.getPlayStatus();
//        return matchPlayerEntity;
//    }
//
//    public MatchPlayer toModel(){
//        return MatchPlayer.builder()
//                .playerNo(playerNo)
//                .memberNo(memberNo)
//                .playStatus(playStatus)
//                .build()
//                ;
//    }
//}
