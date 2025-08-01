package com.sunghyun.football.domain.history.infrastructure.entity;

import com.sunghyun.football.domain.history.domain.MailHistory;
import com.sunghyun.football.global.noti.type.NotiType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MailHistory")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MailHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyNo;

    //유저 번호
//    private Long memberNo;
    //유저 메일
    private String email;

    //발송일자 ex)20240707
    private String sendDt;

    //발송시간 ex)171500
    private String sendTm;

    //발송타입
    private NotiType notiType;

    //발송 제목
    private String subject;

    //발송 내용
    private String content;

    public static MailHistoryEntity of(MailHistory mailHistory) {
        if (mailHistory == null) {
            throw new IllegalArgumentException("mailHistory cannot be null");
        }
        // Optional: 유효성 검사 추가 가능
        return new MailHistoryEntity(
                null,  // historyNo는 DB 생성용 null
//                mailHistory.getMemberNo(),
                mailHistory.getEmail(),
                mailHistory.getSendDt(),
                mailHistory.getSendTm(),
                mailHistory.getNotiType(),
                mailHistory.getSubject(),
                mailHistory.getContent()
        );
    }

    public MailHistory toDomain() {
        return new MailHistory(
                this.historyNo,
//                this.memberNo,
                this.email,
                this.sendDt,
                this.sendTm,
                this.notiType,
                this.subject,
                this.content
        );
    }
}
