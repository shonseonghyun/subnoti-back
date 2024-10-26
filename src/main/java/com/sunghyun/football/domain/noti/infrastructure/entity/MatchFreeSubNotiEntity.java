package com.sunghyun.football.domain.noti.infrastructure.entity;

import com.sunghyun.football.domain.noti.domain.MatchFreeSubNoti;
import com.sunghyun.football.domain.noti.domain.converter.SendFlgConverter;
import com.sunghyun.football.domain.noti.domain.enums.SendFlg;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "FreeSubNoti")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MatchFreeSubNotiEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notiNo;

    private Long matchNo;

    private String email;

    @Convert(converter = SendFlgConverter.class)
    private SendFlg sendFlg;

    private String matchName;

    private String startDt;

    private String startTm;

    private String endTm;

    public static MatchFreeSubNotiEntity from(MatchFreeSubNoti freeSubNoti){
        MatchFreeSubNotiEntity matchFreeSubNotiEntity = new MatchFreeSubNotiEntity();
        matchFreeSubNotiEntity.notiNo=freeSubNoti.getNotiNo();
        matchFreeSubNotiEntity.email=freeSubNoti.getEmail();
        matchFreeSubNotiEntity.matchNo=freeSubNoti.getMatchNo();
        matchFreeSubNotiEntity.sendFlg = freeSubNoti.getSendFlg();
        matchFreeSubNotiEntity.matchName= freeSubNoti.getMatchName();
        matchFreeSubNotiEntity.startDt=freeSubNoti.getStartDt();
        matchFreeSubNotiEntity.startTm=freeSubNoti.getStartTm();
        matchFreeSubNotiEntity.endTm=freeSubNoti.getEndTm();
        return matchFreeSubNotiEntity;
    }

    public MatchFreeSubNoti toModel(){
        return MatchFreeSubNoti.builder()
                .notiNo(notiNo)
                .matchNo(matchNo)
                .email(email)
                .matchName(matchName)
                .startDt(startDt)
                .startTm(startTm)
                .endTm(endTm)
                .build();
    }
}
