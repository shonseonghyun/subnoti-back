package com.sunghyun.football.domain.member.domain.repository;

import com.sunghyun.football.domain.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findByTel(String tel);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByMemberNo(Long memberNo);
//    List<MemberRole> findMemberRoleByMemberNo(Long memberNo);
    Member save(Member member);
    void delete(Member member);
    void flush();
    void deleteAll();

//    Optional<MemberEntity> findEntityByMemberNo(Long memberNo); //여기다가 두자니 도메인 계층이 인프스트럭쳐 계층에 의졶므로 DDD 원칙 위반이다.
}
