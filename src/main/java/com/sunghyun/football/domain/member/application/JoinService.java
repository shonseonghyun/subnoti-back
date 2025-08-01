package com.sunghyun.football.domain.member.application;

import com.sunghyun.football.domain.member.application.dto.MemberJoinReqDto;
import com.sunghyun.football.domain.member.application.dto.MemberJoinResDto;
import com.sunghyun.football.domain.member.domain.Member;
import com.sunghyun.football.domain.member.domain.MemberRole;
import com.sunghyun.football.domain.member.domain.enums.Role;
import com.sunghyun.football.domain.member.domain.repository.MemberRepository;
import com.sunghyun.football.domain.member.domain.service.MemberDuplicationChecker;
import com.sunghyun.football.global.event.event.NotificationSentEvent;
import com.sunghyun.football.global.noti.message.build.dto.JoinMessageDto;
import com.sunghyun.football.global.noti.type.NotiType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final MemberRepository memberRepository;
    private final MemberDuplicationChecker memberDuplicationChecker;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public boolean checkEmailDuplication(String email){
        Optional<Member> selectedMember = memberRepository.findByEmail(email);
        return selectedMember.isPresent();
    }

    @Transactional
    public MemberJoinResDto join(MemberJoinReqDto memberJoinReqDto) {
        memberDuplicationChecker.checkDuplicatiedFields(memberJoinReqDto);

        //여기다 도메인 생성을 두자니 난잡한데...
        //그렇다고 도메인생성을 도메인에게 위임하고 MemberJoinReqDto를 파라미터로 넘기자니 패키지 양방향 의존관계되서 안된다..
        Member member = Member.builder()
                .email(memberJoinReqDto.getEmail())
                .pwd(passwordEncoder.encode(memberJoinReqDto.getPwd()))
                .name(memberJoinReqDto.getName())
                .tel(memberJoinReqDto.getTel())
                .birthDt(memberJoinReqDto.getBirthDt())
                .gender(memberJoinReqDto.getGender())
//                .level(MemberLevelType.ROOKIE)
                .role(List.of(new MemberRole(Role.USER)))
                .build()
                ;

        Member savedMember = memberRepository.save(member);

        eventPublisher.publishEvent( NotificationSentEvent.builder()
                .notiType(NotiType.NOTI_JOIN)
                .email(member.getEmail())
                .messageDto(new JoinMessageDto(member.getName()))
                .build()
        );

        return MemberJoinResDto.from(savedMember);
    }
}
