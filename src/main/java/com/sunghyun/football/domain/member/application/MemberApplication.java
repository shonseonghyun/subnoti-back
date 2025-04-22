    package com.sunghyun.football.domain.member.application;

    import com.sunghyun.football.domain.member.application.dto.SelectMemberResDto;
    import com.sunghyun.football.domain.member.domain.Member;
    import com.sunghyun.football.domain.member.domain.repository.MemberRepository;
    import com.sunghyun.football.domain.member.domain.dto.MemberUpdReqDto;
    import com.sunghyun.football.global.utils.MergeUtils;
    import lombok.RequiredArgsConstructor;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    import org.springframework.util.StringUtils;

    @Service
    @RequiredArgsConstructor
    public class MemberApplication {

        private final MemberRepository memberRepository;
        private final MemberServiceHelper memberServiceHelper;
        private final PasswordEncoder passwordEncoder;

        @Transactional
        public SelectMemberResDto getMember(Long memberNo) {
            Member selectedMember = memberServiceHelper.findExistingMember(memberNo);

            return SelectMemberResDto.from(selectedMember);
        }

        @Transactional
        public void updateMember(final Long memberNo,final MemberUpdReqDto source){
            Member selectedMember = memberServiceHelper.findExistingMember(memberNo);

            //비밀번호만 따라 인코딩
            if (StringUtils.hasText(source.getPwd())) {
                String encodedPwd = passwordEncoder.encode(source.getPwd());
                selectedMember.changePwd(encodedPwd); // 인코딩된 값을 도메인에게 넘김
            }

            selectedMember.updateMember(
                    source.getName(),
                    source.getGender(),
                    source.getTel()
            );

            memberRepository.save(selectedMember);
        }

        public void deleteMember(Long memberNo) {
            Member selectedMember = memberServiceHelper.findExistingMember(memberNo);

            memberRepository.delete(selectedMember);
        }


    }
