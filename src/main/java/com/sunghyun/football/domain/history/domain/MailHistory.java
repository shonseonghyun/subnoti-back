package com.sunghyun.football.domain.history.domain;

import com.sunghyun.football.global.noti.type.NotiType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MailHistory {
    private Long historyNo;

//    private Long memberNo;
    private String email;

    private String sendDt;

    private String sendTm;

    private NotiType notiType;

    private String subject;

    private String content;

    public static MailHistory createNewMailHistory(String email, String sendDt, String sendTm,
                                        NotiType notiType, String subject, String content){
        // 유효성 검사 가능
//        if (subject == null || subject.isBlank()) {
//            throw new IllegalArgumentException("subject cannot be empty");
//        }
        return new MailHistory(null, email, sendDt, sendTm, notiType, subject, content);
    }
}
