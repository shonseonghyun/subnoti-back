package com.sunghyun.football.domain.member.infrastructure;

import com.sunghyun.football.domain.member.infrastructure.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringJpaMemberRepository extends JpaRepository<MemberEntity,Long> {
    Optional<MemberEntity> findByTel(String tel);
    Optional<MemberEntity> findByEmail(String email);
    Optional<MemberEntity> findByMemberNo(Long memberNo);
}
