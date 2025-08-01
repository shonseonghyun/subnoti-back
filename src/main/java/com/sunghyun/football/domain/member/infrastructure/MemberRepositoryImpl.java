package com.sunghyun.football.domain.member.infrastructure;

import com.sunghyun.football.domain.member.domain.Member;
import com.sunghyun.football.domain.member.domain.repository.MemberRepository;
import com.sunghyun.football.domain.member.infrastructure.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final SpringJpaMemberRepository springJpaMemberRepository;
    private final SpringJpaMemberRoleRepository springJpaMemberRoleRepository;

    @Override
    public Optional<Member> findByTel(String tel) {
        return springJpaMemberRepository.findByTel(tel).map(MemberEntity::toModel);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return springJpaMemberRepository.findByEmail(email).map(MemberEntity::toModel);
    }

    @Override
    public Optional<Member> findByMemberNo(Long memberNo) {
        return springJpaMemberRepository.findByMemberNo(memberNo).map(MemberEntity::toModel);
    }

    @Override
    public Member save(Member member) {
        return springJpaMemberRepository.save(MemberEntity.from(member)).toModel();
    }

    @Override
    public void delete(Member member) {
        springJpaMemberRepository.delete(MemberEntity.from(member));
    }

    @Override
    public void flush() {
        springJpaMemberRepository.flush();
    }

    @Override
    public void deleteAll() {
        springJpaMemberRepository.deleteAll();
    }


    /** ===========================
     * ✅ 인프라스트럭처/JPA 동작 검증 전용 메서드 (테스트에서만 사용)
     *  - Lazy 로딩, fetch join 등 JPA 자체 동작 검증 가능
     * ============================ */
    public Optional<MemberEntity> findEntityByMemberNo(Long memberNo) {
        return springJpaMemberRepository.findByMemberNo(memberNo);
    }

}
