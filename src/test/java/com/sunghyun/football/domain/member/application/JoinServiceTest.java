package com.sunghyun.football.domain.member.application;

import com.sunghyun.football.domain.member.application.dto.MemberJoinReqDto;
import com.sunghyun.football.domain.member.application.dto.MemberJoinResDto;
import com.sunghyun.football.domain.member.domain.Member;
import com.sunghyun.football.domain.member.domain.enums.Gender;
import com.sunghyun.football.domain.member.domain.repository.MemberRepository;
import com.sunghyun.football.domain.member.domain.service.MemberDuplicationChecker;
import com.sunghyun.football.global.event.event.NotificationSentEvent;
import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.member.exception.JoinException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@SpringBootTest
//@ActiveProfiles("local") // ✅ 테스트 시 local 설정만 사용
@ExtendWith(MockitoExtension.class)
class JoinServiceTest {
    private final String email = "sunghyun7895@naver.com";
    private final String tel = "030213";
    private final String pwd = "1234";
    private final String name = "네임";
    private final String birthDt = "19940202";
    private final Gender gender = Gender.MALE;

    @InjectMocks
    private JoinService target;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberDuplicationChecker memberDuplicationChecker;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    /**
     * @SpringBooTest로 테스트 시에 해당되는 이야기다.
     * MockBean으로 가상객체를 할당함.
     * 해당 테스트의 목적은 "회원가입 작동"에 초점을 두었기 때문에 이벤트 리스너 세부 작동까지 확인할 필요는 없기 때문이다.
     */
//    @Autowired
//    private MailHistoryRepository mailHistoryRepository;

    @Test
    @DisplayName("동일한 이메일을 가진 경우, 회원가입은 실패된다.")
    void memberJoinFailByDuplicatedEmail(){
        //given
        MemberJoinReqDto duplicatedEmailMemberJoinReqDto = createMemberJoinReqDto();
        doThrow(new JoinException(ErrorType.DUPLICATED_EMAIL_REGISTER))
                .when(memberDuplicationChecker)
                .checkDuplicatiedFields(any())
        ;


        //when,then
        assertThatThrownBy(()->target.join(duplicatedEmailMemberJoinReqDto))
                .isInstanceOf(JoinException.class)
        ;

        verify(memberRepository,times(0)).save(any());
        verify(eventPublisher, times(0)).publishEvent(any(NotificationSentEvent.class));
    }

    @Test
    @DisplayName("이메일이 아닌 이미 db상 존재하는 필드의 값을 요청 받은 경우, 회원가입은 성공된다.")
    void memberJoinSuccessByDuplicatedTel(){
        //given
        MemberJoinReqDto duplicatedTelMemberJoinReqDto = createMemberJoinReqDto();
        when(memberRepository.save(any()))
                .thenReturn(Member.builder()
                                .memberNo(1L)
                                .email(duplicatedTelMemberJoinReqDto.getEmail())
                                .name(duplicatedTelMemberJoinReqDto.getName())
                                .tel(duplicatedTelMemberJoinReqDto.getTel())
                                .build()
                )
        ;

        //when
        MemberJoinResDto result = target.join(duplicatedTelMemberJoinReqDto);

        //then
        assertThat(result.getMemberNo()).isEqualTo(1L);

        verify(memberRepository,times(1)).save(any());
        verify(eventPublisher, times(1)).publishEvent(any(NotificationSentEvent.class));
//        테스트 실패: Spring의 실제 빈 등록과 이벤트 리스너가 동작하지 않는다. 따라서 NofiticationSendEventHandler 또한 동작하지 않고, 따라서 notificationFacade.notify도 호출되지 않는다.
//        verify(notificationFacade,times(0)).notify(any(),anyString(),any());
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