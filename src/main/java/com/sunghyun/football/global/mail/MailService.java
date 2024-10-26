package com.sunghyun.football.global.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    public void send(/*MailReqDto mailReqDto*/){
        log.info("발송");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        simpleMailMessage.setTo(mailReqDto.getEmail());
//        simpleMailMessage.setSubject(mailReqDto.getMailType().getSubject());
//        simpleMailMessage.setText(mailReqDto.);
        simpleMailMessage.setTo("sunghyun7895@naver.com");
        simpleMailMessage.setSubject("제목");
        simpleMailMessage.setText("내용");
        javaMailSender.send(simpleMailMessage);
    }

}
