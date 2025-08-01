package com.sunghyun.football.domain.history.infrastructure.repository;

import com.sunghyun.football.domain.history.domain.MailHistory;
import com.sunghyun.football.domain.history.domain.repository.MailHistoryRepository;
import com.sunghyun.football.domain.history.infrastructure.entity.MailHistoryEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MailHistoryRepositoryImpl implements MailHistoryRepository {
    private final SpringJpaMailHistoryRepository springJpaMailHistoryRepository;

    @Override
    public MailHistory save(final MailHistory mailHistory) {
        return springJpaMailHistoryRepository.save(MailHistoryEntity.of(mailHistory)).toDomain();
    }

    @Override
    public List<MailHistory> findAll() {
        return springJpaMailHistoryRepository.findAll().stream().map(MailHistoryEntity::toDomain).toList();
    }

    @Override
    public void deleteAll() {
        springJpaMailHistoryRepository.deleteAll();
    }
}
