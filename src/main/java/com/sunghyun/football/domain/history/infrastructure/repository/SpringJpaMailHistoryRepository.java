package com.sunghyun.football.domain.history.infrastructure.repository;

import com.sunghyun.football.domain.history.infrastructure.entity.MailHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringJpaMailHistoryRepository extends JpaRepository<MailHistoryEntity,Long> {
    MailHistoryEntity findByEmail(final String email);
}
