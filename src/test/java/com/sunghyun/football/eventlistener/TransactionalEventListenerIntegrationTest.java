package com.sunghyun.football.eventlistener;

import com.sunghyun.football.config.TestConfig;
import com.sunghyun.football.domain.history.domain.repository.MailHistoryRepository;
import com.sunghyun.football.domain.member.application.JoinService;
import com.sunghyun.football.domain.member.application.dto.MemberJoinReqDto;
import com.sunghyun.football.domain.member.application.dto.MemberJoinResDto;
import com.sunghyun.football.domain.member.domain.enums.Gender;
import com.sunghyun.football.domain.member.domain.repository.MemberRepository;
import com.sunghyun.football.global.event.handler.NotificationSendEventHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@Import(TestConfig.class)
@SpringBootTest
@ActiveProfiles("local")
public class TransactionalEventListenerIntegrationTest {

    private final String email = "sunghyun7895@naver.com";
    private final String tel = "030213";
    private final String pwd = "1234";
    private final String name = "네임";
    private final String birthDt = "19940202";
    private final Gender gender = Gender.MALE;

    @Autowired
    private JoinService joinService;

    @Autowired
    private MailHistoryRepository mailHistoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @SpyBean
    private NotificationSendEventHandler NotificationSendEventHandler;

    @AfterEach
    void cleanUp() {
        mailHistoryRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("이벤트 리스너 내부 예외 발생 시 이벤트 리스너는 롤백, 회원가입은 성공")
    void eventListenerFail(){
        //given
        doThrow(new RuntimeException("테스트용 강제 예외"))
                .when(NotificationSendEventHandler)
                .sendNotification(any());

        //when
        MemberJoinResDto memberJoinResDto = joinService.join(createMemberJoinReqDto());

        //then
        assertThat(mailHistoryRepository.findAll().size()).isEqualTo(0); //메일 저장은 실패
        assertThat(memberJoinResDto.getMemberNo()).isNotNull(); // 회원가입은 성공
    }

    @Test
    @DisplayName("이벤트 리스너 정상 처리 시, 이벤트 리스너 및 회원가입 성공")
    void eventListenerSuccess() throws InterruptedException {
        //when
        MemberJoinResDto memberJoinResDto = joinService.join(createMemberJoinReqDto());

        Thread.sleep(5000); //테스트가 끝나기 전에 비동기 처리가 끝나지 않는 문제 해결하기 위해 추가

        //then
        assertThat(memberJoinResDto.getMemberNo()).isNotNull();
        assertThat(mailHistoryRepository.findAll().size()).isEqualTo(1);
    }

    private MemberJoinReqDto createMemberJoinReqDto(){
        return  MemberJoinReqDto.builder()
                .email(email)
                .pwd(pwd)
                .pwdConfirm(pwd)
                .name(name)
                .birthDt(birthDt)
                .gender(gender)
                .tel(tel)
                .build()
                ;
    }
}
