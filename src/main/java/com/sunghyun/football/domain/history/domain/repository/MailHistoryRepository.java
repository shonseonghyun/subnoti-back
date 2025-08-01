package com.sunghyun.football.domain.history.domain.repository;

import com.sunghyun.football.domain.history.domain.MailHistory;

import java.util.List;

public interface MailHistoryRepository {
    MailHistory save(final MailHistory mailHistory);
    List<MailHistory> findAll();
    void deleteAll();
}
