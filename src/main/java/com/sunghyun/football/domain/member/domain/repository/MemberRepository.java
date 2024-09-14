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
}
