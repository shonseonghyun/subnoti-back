package com.sunghyun.football.global.noti.mail;

import com.sunghyun.football.global.noti.NotiProcessor;
import com.sunghyun.football.global.noti.NotiSendReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailNotiProcessor extends NotiProcessor {
    private final JavaMailSender javaMailSender;

    public void doNoti(NotiSendReqDto notiSendReqDto){
        log.info("[{}] 메일 발송",notiSendReqDto.getSubject());
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(notiSendReqDto.getEmail());
        simpleMailMessage.setSubject(notiSendReqDto.getSubject());
        simpleMailMessage.setText(notiSendReqDto.getContent());
        javaMailSender.send(simpleMailMessage);
    }

}
