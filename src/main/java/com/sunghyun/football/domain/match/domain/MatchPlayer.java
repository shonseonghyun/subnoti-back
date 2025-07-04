//package com.sunghyun.football.domain.match.domain;
//
//import com.sunghyun.football.domain.match.domain.enums.PlayStatus;
//import lombok.*;
//
//import java.util.Objects;
//
//@Getter
//@Setter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class MatchPlayer {
//    private Long playerNo;
//
//    private Long memberNo;
//
//    private PlayStatus playStatus;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        MatchPlayer that = (MatchPlayer) o;
//        return Objects.equals(getMemberNo(), that.getMemberNo());
//    }
//
//    public static MatchPlayer createPlayer(Long memberNo){
//        MatchPlayer player= new MatchPlayer();
//        player.memberNo = memberNo;
//        player.playStatus=PlayStatus.MATCH_START_BEFORE;
//        return player;
//    }
//}
